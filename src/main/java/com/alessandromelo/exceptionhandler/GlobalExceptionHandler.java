package com.alessandromelo.exceptionhandler;

import com.alessandromelo.exception.customer.CpfAlreadyExistsException;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.customer.PhoneNumberAlreadyExistsException;
import com.alessandromelo.exception.global.EntityInUseException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//CUSTOMER:

    @ExceptionHandler(CustomerNotFoundException.class) //404
    public ResponseEntity<ApiError> handleCustomerNotFound(CustomerNotFoundException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CpfAlreadyExistsException.class) //409
    public ResponseEntity<ApiError> handleCpfAlreadyExists(CpfAlreadyExistsException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class) //409
    public ResponseEntity<ApiError> handlePhoneNumberAlreadyExists(PhoneNumberAlreadyExistsException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

//GLOBAL:

    @ExceptionHandler(EntityInUseException.class) //409
    public ResponseEntity<ApiError> handleEntityInUseExeption(EntityInUseException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
