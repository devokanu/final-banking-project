package com.okan.bankingmanagement.exception;

import java.io.IOException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.authentication.DisabledException;
import com.okan.bankingmanagement.domain.HttpResponse;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandling implements ErrorController {
	
	
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String UNAUTHORIZED_ACCESS = "Access Denied";
    public static final String ERROR_PATH = "/error";

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExist(UsernameExistException exception){
    	return createHttpResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    
    }
    
    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExist(EmailExistException exception){
    	return createHttpResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    
    }
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(FORBIDDEN, ACCOUNT_DISABLED);
    }
    
    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<HttpResponse> invalidAccountTypeException(InvalidAccountTypeException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(AccountAuthorizationException.class)
    public ResponseEntity<HttpResponse> accountAuthorizationException(AccountAuthorizationException exception) {
        return createHttpResponse(UNAUTHORIZED, UNAUTHORIZED_ACCESS);
    }
    
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<HttpResponse> accountNotFoundException(AccountNotFoundException exception) {
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }
    
    @ExceptionHandler(DeletedAccountException.class)
    public ResponseEntity<HttpResponse> deletedAccountException(DeletedAccountException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<HttpResponse> invalidInputException(InvalidInputException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<HttpResponse> insufficientFundsException(InsufficientFundsException exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(BankExistException.class)
    public ResponseEntity<HttpResponse> bankExistException(BankExistException exception) {
        return createHttpResponse(UNPROCESSABLE_ENTITY, exception.getMessage());
    }
    
    
    
    
    


    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
    
    

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<HttpResponse> notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }
    
    

   

}
