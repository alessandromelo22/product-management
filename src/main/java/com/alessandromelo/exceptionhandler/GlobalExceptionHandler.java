package com.alessandromelo.exceptionhandler;

import com.alessandromelo.exception.customer.CpfAlreadyExistsException;
import com.alessandromelo.exception.customer.CustomerNotFoundException;
import com.alessandromelo.exception.customer.PhoneNumberAlreadyExistsException;
import com.alessandromelo.exception.global.EntityInUseException;
import com.alessandromelo.exception.product.ProductNotFoundException;
import com.alessandromelo.exception.user.EmailNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

//CUSTOMER:

    @ExceptionHandler(CustomerNotFoundException.class) //404
    public ResponseEntity<ApiError> handleCustomerNotFoundException(CustomerNotFoundException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CpfAlreadyExistsException.class) //409
    public ResponseEntity<ApiError> handleCpfAlreadyExistsException(CpfAlreadyExistsException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class) //409
    public ResponseEntity<ApiError> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

//PRODUCT:

    @ExceptionHandler(ProductNotFoundException.class)//404
    public ResponseEntity<ApiError> handleProductNotFoundException(ProductNotFoundException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


//USER:
    @ExceptionHandler(EmailNotFoundException.class) //404
    public ResponseEntity<ApiError> handleEmailNotFoundException(EmailNotFoundException exception,
                                                                 HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


//GLOBAL:

    @ExceptionHandler(EntityInUseException.class) //409
    public ResponseEntity<ApiError> handleEntityInUseException(EntityInUseException exception, HttpServletRequest request){

        ApiError error = new ApiError(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

//MVC:

    @ExceptionHandler(MethodArgumentNotValidException.class)//400
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                                HttpServletRequest request){

        String errorsMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("||"));

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,errorsMessages, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)//400
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
                                                                              HttpServletRequest request){

        String errorMessage = String.format("Value '%s' is not valid for field '%s' expected type: '%s'",
                exception.getValue(), exception.getName(), exception.getRequiredType().getSimpleName());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
