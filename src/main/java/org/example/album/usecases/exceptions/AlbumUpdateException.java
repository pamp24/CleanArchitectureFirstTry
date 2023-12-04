package org.example.album.usecases.exceptions;

import org.example.shared.exceptions.BlogapiException;
import org.springframework.http.HttpStatus;

public class AlbumUpdateException extends BlogapiException {
    public AlbumUpdateException(HttpStatus status, String message) {
        super(status, message);
    }

    public AlbumUpdateException(HttpStatus status, String message, Throwable exception) {
        super(status, message, exception);
    }
}
