package com.dm.micropifs.util;


import com.dm.micropifs.model.APIError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(SocketTimeoutException.class)
    protected ResponseEntity<Object> handleSocketTimeoutException( SocketTimeoutException e ) {
        return buildResponseEntity(e, "Socket timeout! ", HttpStatus.REQUEST_TIMEOUT);
    }



    private ResponseEntity<Object> buildResponseEntity(Exception e, String message, HttpStatus status) {

        System.out.println("CUSTOMERRR" + e.getMessage());
        APIError apiError = new APIError(status);
        apiError.setMessage(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }





}





