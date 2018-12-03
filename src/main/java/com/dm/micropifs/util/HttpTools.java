package com.dm.micropifs.util;

import com.dm.micropifs.fileio.DataStore;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.capitalize;

@Service
public class HttpTools {

    private final DataStore dataStore;

    @Inject
    public HttpTools(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public HttpHeaders composeCamHeaders(){

        HttpHeaders headers = new HttpHeaders();

        try {
            buildHeaderList(dataStore.getCurrentLog()).forEach(headers::add);
        } catch (Exception e){
            headers.add("Data-Error", e.getMessage());
        }

        return headers;
    }


    private static Map<String,String> buildHeaderList(String raw){

        Map<String,String> headers = new HashMap<>();

        String nl = raw.contains("\r\n") ? "\r\n" : "\n";
        String [] lines = raw.split(nl);

        for (String l : lines) {

            String [] pair = l.split(":");
            String k = validateKey(pair[0]);
            String v = (pair.length > 1) ? l.substring(pair[0].length()+1,l.length()-1) : "no data";

            headers.put(k, v.trim());
        }
        return headers;
    }

    private static String validateKey(String raw){

        List<String> pieces = new ArrayList<>();
        String [] x = raw.split("[^a-zA-Z\\d:]");

        for (String w : x)
            if (!w.isEmpty()) {
                pieces.add(capitalize(w));
            }

        return String.join("-",pieces.toArray(new String[0]));
    }

}
