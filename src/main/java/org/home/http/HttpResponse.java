package org.home.http;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static final String VERSION = "HTTP/1.1";
    private static final String CRLF = "\r\n";

    private final HttpStatus status;
    private final List<HttpHeader> headers;

    private final String body;

    public HttpResponse(HttpStatus status, String body) {
        this.status = status;
        this.headers = new ArrayList<>();
        this.body = body;
        addContentLength();
    }

    private void addContentLength() {
        addHeader(HttpHeader.CONTENT_LENGTH, Integer.toString(this.body.getBytes().length));
    }

    private HttpResponse addHeader(String name, String value) {
        this.headers.add(new HttpHeader(name, value));
        return this;
    }

    public byte[] getBytes() {
        return getString().getBytes();
    }
    public String getString() {
        StringBuilder sb = new StringBuilder();

        sb.append(VERSION).append(" ").append(status.getCode()).append(" ").append(status.getMessage()).append(CRLF);

        for (HttpHeader header : headers) {
            sb.append(header.getName()).append(": ").append(header.getValue()).append(CRLF);
        }

        sb.append(CRLF).append(body).append(CRLF).append(CRLF);

        return sb.toString();
    }

}
