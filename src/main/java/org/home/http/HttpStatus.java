package org.home.http;

public enum HttpStatus {

    OK(200, Series.SUCCESSFUL, "OK"),
    BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
    METHOD_NOT_ALLOWED(401, Series.CLIENT_ERROR, "Method Not Allowed"),
    URI_TO_LONG(414, Series.CLIENT_ERROR, "URI Too Long"),

    INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"),
    HTTP_VERSION_NOT_SUPPORTED(505, Series.SERVER_ERROR, "HTTP Version not supported"),
    ;

    private final int code;
    private final Series series;
    private final String message;

    HttpStatus(int code, Series series, String message) {
        this.code = code;
        this.series = series;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        private Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
