package org.example.user.usecases.exceptions;

import org.example.shared.exceptions.BlogapiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BlogapiException {
    static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;
    static final String MESSAGE = "user.errors";

    public UserAlreadyExistsException(){
        super( HTTP_STATUS, MESSAGE);
    }
    public UserAlreadyExistsException(String message){
        super( HTTP_STATUS, MESSAGE);
    }
}
