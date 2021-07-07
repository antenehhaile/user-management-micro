package com.user.management.controller.resolver;

import com.user.management.exceptions.UserApiDatabaseException;
import com.user.management.exceptions.UserApiRcpException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserApiDatabaseException.class)
    public ResponseEntity<String> handleUserApiDatabaseException() {
        return new ResponseEntity<>("Internal error has occured while processing the data.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserApiRcpException.class)
    public ResponseEntity<String> handleUserApiRcpException() {
        return new ResponseEntity<>("Internal error has occured while transporting the data.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
