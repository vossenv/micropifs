package com.dm.micropifs.util;


import com.dm.micropifs.model.APIError;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketTimeoutException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final ExtendedLogger el;
    private final static Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @Inject
    public RestExceptionHandler(ExtendedLogger el) {
        this.el = el;
    }


    @ExceptionHandler({IOException.class, ClientAbortException.class, SocketTimeoutException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ResponseEntity<Object> handleIOException(Exception e, HttpServletRequest request) {
        logger.error("IO Handler ::: " + el.getRequestError(request, e));
        return null;
    }

//    private ResponseEntity<Object> buildResponseEntity(Exception e, HttpServletRequest request, HttpStatus status) {
//        APIError apiError = new APIError(status);
//        apiError.setMessage(e.getMessage());
//        return new ResponseEntity<>(apiError, apiError.getStatus());
//    }

}





