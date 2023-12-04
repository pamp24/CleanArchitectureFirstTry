package org.example.category.usecases.exceptions;

import org.example.shared.exceptions.BlogapiException;
import org.springframework.http.HttpStatus;

public class CategoryUpdateException extends BlogapiException {
    public CategoryUpdateException(HttpStatus status, String message) {
        super(status, message);
    }

    public CategoryUpdateException(HttpStatus status, String message, Throwable exception) {
        super(status, message, exception);
    }
}
