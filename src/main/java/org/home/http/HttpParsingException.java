package org.home.http;

public class HttpParsingException extends Exception {

    private final HttpStatus errorCode;

    public HttpParsingException(HttpStatus errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
