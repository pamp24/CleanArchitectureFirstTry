package org.example.album.usecases.exceptions;

import org.example.shared.exceptions.BlogapiException;
import org.springframework.http.HttpStatus;

public class AlbumDeleteException extends BlogapiException {


    public AlbumDeleteException(HttpStatus status, String message) {
        super(status, message);
    }

    public AlbumDeleteException(HttpStatus status, String message, Throwable exception) {
        super(status, message, exception);
    }
}
