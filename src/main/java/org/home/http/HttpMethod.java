package org.home.http;

public enum HttpMethod {

    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE;

    public static final int MAX_LENGTH;

    static {
        int temp = -1;
        for (HttpMethod method : values()) {
            if (method.name().length() > temp) {
                temp = method.name().length();
            }
        }
        MAX_LENGTH = temp;
    }


}
