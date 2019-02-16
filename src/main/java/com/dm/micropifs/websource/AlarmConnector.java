package com.dm.micropifs.websource;

import com.dm.micropifs.MicrocamPifs;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class AlarmConnector {

    @Inject
    private Environment env;
    private String lloydUser = "default";
    private String lloydPass = "default";
    private String baseURL = "https://www.alarm.com/web/Video/GetImage.aspx?res=0&qual=10&deviceID=";
    private HttpHeaders authHeaders;
    private LocalDateTime lastAuth;
    private final static Logger logger = LogManager.getLogger(AlarmConnector.class);

    @PostConstruct
    void test() {
        this.lloydUser = env.getProperty("lloyd.username");
        this.lloydPass = env.getProperty("lloyd.password");
        this.lloydUser = this.lloydUser != null ? this.lloydUser : System.getenv("LLOYD_USERNAME");
        this.lloydPass = this.lloydPass != null ? this.lloydPass : System.getenv("LLOYD_PASSWORD");
        authenticate();
    }

    public byte[] getImage(String target) {

        LocalDateTime curTime = LocalDateTime.now();

        if (Duration.between(curTime, lastAuth).toHours() > 6){
            try {
                authenticate();
            } catch (Exception e) {
                logger.error("Authentication failed, reason: "+ e.getMessage());
            }
        }

        String url = baseURL + target;
        HttpEntity<String> request = new HttpEntity<>(this.authHeaders);
        ResponseEntity<byte[]> response = new RestTemplate().exchange(url, HttpMethod.GET, request, byte[].class);

        String b = new String(response.getBody());
        if (response.getStatusCodeValue() != 200 || b.contains("Login") || b.contains("Log")){
            try {
                logger.error("Response from alarm.com unsuccessful, preforming re-authentication... ");
                authenticate();
            } catch (Exception e) {
                logger.error("Authentication failed, reason: "+ e.getMessage());
            }
        }
        return response.getBody();
    }

    private void authenticate() {

        logger.debug("Beginning alarm.com authentication... ");
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().disableRedirectHandling().build());
        RestTemplate restTemplate = new RestTemplate(factory);
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String url = "https://www.alarm.com/web/Default.aspx";
        StringBuilder cookies = new StringBuilder("cookieTest=1;IsFromNewSite=1;loggedInAsSubscriber=1;");
        Set<String> authParams = new HashSet<>(Arrays.asList("afg", "auth_CustomerDotNet", "twoFactorAuthenticationId", "ASP.NET_SessionId"));

        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        body.add("IsFromNewSite", "1");
        body.add("ctl00$ContentPlaceHolder1$loginform$txtUserName", this.lloydUser);
        body.add("txtPasswordt", this.lloydPass);

        HttpEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        for (String v : new HashSet<>(response.getHeaders().get("Set-Cookie"))) {
            String t = v.split(";")[0];
            if (authParams.contains(t.split("=")[0])) {
                cookies.append(t).append(";");
            }
        }

        headers.add("Cookie", cookies.toString());
        this.authHeaders = headers;
        logger.debug("Finished alarm.com authentication... ");
        lastAuth = LocalDateTime.now();
    }

}



