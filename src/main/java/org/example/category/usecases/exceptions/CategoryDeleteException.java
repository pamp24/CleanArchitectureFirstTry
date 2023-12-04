package org.example.category.usecases.exceptions;

import org.example.shared.exceptions.BlogapiException;
import org.springframework.http.HttpStatus;

public class CategoryDeleteException extends BlogapiException {

    public CategoryDeleteException(HttpStatus status, String message) {
        super(status, message);
    }

    public CategoryDeleteException(HttpStatus status, String message, Throwable exception) {
        super(status, message, exception);
    }
}
