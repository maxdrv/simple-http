package org.home.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final static Logger log = LoggerFactory.getLogger(HttpParser.class);

    private final static int SP = 0x20;  // 32 space
    private final static int CR = 0x0D;  // 13 carriage return
    private final static int LF = 0x0A;  // 10 line feed

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parseHeaders(reader, request);
        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder buffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int next;
        while ((next = reader.read()) >= 0) {
            if (next == CR) {
                next = reader.read();
                if (next == LF) {
                    if (!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(HttpStatus.BAD_REQUEST);
                    }

                    try {
                        request.setHttpVersion(buffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatus.BAD_REQUEST);
                    }

                    return;
                } else {
                    throw new HttpParsingException(HttpStatus.BAD_REQUEST);
                }
            }

            if (next == SP) {
                if (!methodParsed) {
                    log.debug("Request Line METHOD to Process: {}", buffer);
                    request.setMethod(buffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    log.debug("Request Line REQUEST TARGET to Process: {}", buffer);
                    request.setRequestTarget(buffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatus.BAD_REQUEST);
                }
                log.debug("Request Line to Process: {}", buffer);
                buffer.delete(0, buffer.length());
            } else {
                buffer.append((char) next);
                if (!methodParsed) {
                    if (buffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatus.NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }
    private void parseHeaders(InputStreamReader reader, HttpRequest request) {

    }


    private void parseBody(InputStreamReader reader, HttpRequest request) {

    }

}
