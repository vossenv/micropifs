package com.dm.micropifs.util;


import com.dm.micropifs.model.APIError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketTimeoutException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler  extends ResponseEntityExceptionHandler {


    @ExceptionHandler(SocketTimeoutException.class)
    protected ResponseEntity<Object> handleSocketTimeoutException( SocketTimeoutException e) {

        System.out.println(e.getMessage() + " SOCKET XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }


    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(IOException e) {

        System.out.println(e.getMessage() + " IO XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }

//    @ExceptionHandler(IOException.class)
//    protected ResponseEntity<Object> handleIOException2(IOException e) {
//        return buildResponseEntity2(e, HttpStatus.REQUEST_TIMEOUT);
//    }

    private ResponseEntity<Object> buildResponseEntity(Exception e, HttpServletRequest request, HttpStatus status) {

        System.out.println("CUSTOMERRR" + e.getMessage());
        System.out.println(request.getHeaderNames());
        APIError apiError = new APIError(status);
        apiError.setMessage(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<Object> buildResponseEntity2(Exception e, HttpStatus status) {

        System.out.println("CUSTOMERRR" + e.getMessage());
        APIError apiError = new APIError(status);
        apiError.setMessage(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }



}





