package com.thetechmaddy.ecommerce.controllers;

import com.thetechmaddy.ecommerce.exceptions.ApiException;
import com.thetechmaddy.ecommerce.models.responses.ApiResponse;
import com.thetechmaddy.ecommerce.models.responses.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Log4j2
@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> runtimeException(RuntimeException ex) {
        log.error(ex);
        var response = ApiResponse.error(new ErrorResponse("Something went wrong!"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> apiException(ApiException ex) {
        log.error(ex);
        var response = ApiResponse.error(new ErrorResponse(ex.getMessage()));
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex);
        var response = ApiResponse.error(new ErrorResponse("Data integrity violation"));
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> routeNotFoundException(NoHandlerFoundException ex) {
        log.error(ex);
        var response = ApiResponse.error(new ErrorResponse("Requested endpoint not found"));
        return ResponseEntity.status(NOT_FOUND).body(response);
    }
}
