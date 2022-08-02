package org.home;

import org.home.http.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HttpParserTest {

    private HttpParser parser;

    @BeforeAll
    public void beforeAll() {
        parser = new HttpParser();
    }

    @Test
    void success() {
        HttpRequest request = null;
        try {
            request = parser.parseHttpRequest(generateGetValid());
        } catch (HttpParsingException e) {
            fail(e);
        }
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
    }

    @Test
    void badHttpMethod() {
        try {
            parser.parseHttpRequest(generateBadMethod());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Test
    void tooLongMethodName() {
        try {
            parser.parseHttpRequest(generateBadTooLongMethod());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void requestLineInvalidAmountOfItems() {
        try {
            parser.parseHttpRequest(generateTooManyItemsInRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @Test
    void emptyRequestLine() {
        try {
            parser.parseHttpRequest(generateEmptyRequestLine());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void requestLineHasCRnoLF() {
        try {
            parser.parseHttpRequest(generateNoLineFeed());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void requestHasBadHttpVersion() {
        try {
            parser.parseHttpRequest(generateBadHttpVersion());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void requestHasUnsupportedVersion() {
        try {
            parser.parseHttpRequest(generateUnsupportedVersion());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
    void requestHasSupportedVersion() {
        try {
            var request = parser.parseHttpRequest(generateSupportedVersion());

            assertNotNull(request);
            assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
            assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
        } catch (HttpParsingException e) {
            fail(e);
        }
    }


    private InputStream generateGetValid() {
        String raw = "GET / HTTP/1.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Connection: keep-alive\r\n" +
                     "sec-ch-ua: \".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"\r\n" +
                     "sec-ch-ua-mobile: ?0\r\n" +
                     "sec-ch-ua-platform: \"Windows\"\r\n" +
                     "Upgrade-Insecure-Requests: 1\r\n" +
                     "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36\r\n" +
                     "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
                     "Sec-Fetch-Site: none\r\n" +
                     "Sec-Fetch-Mode: navigate\r\n" +
                     "Sec-Fetch-User: ?1\r\n" +
                     "Sec-Fetch-Dest: document\r\n" +
                     "Accept-Encoding: gzip, deflate, br\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateBadMethod() {
        String raw = "Get / HTTP/1.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateBadTooLongMethod() {
        String raw = "GET / AAA HTTP/1.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateTooManyItemsInRequestLine() {
        String raw = "GETTTTTTTTTTT / HTTP/1.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateEmptyRequestLine() {
        String raw = "\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateNoLineFeed() {
        String raw = "GET / HTTP/1.1\r" +  // <--- no line feed
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateBadHttpVersion() {
        String raw = "GET / HTP/1.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateUnsupportedVersion() {
        String raw = "GET / HTTP/2.1\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private InputStream generateSupportedVersion() {
        String raw = "GET / HTTP/1.2\r\n" +
                     "Host: localhost:4000\r\n" +
                     "Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7\r\n" +
                     "\r\n";
        return toInputStream(raw);
    }

    private static InputStream toInputStream(String raw) {
        byte[] usASCIIEncoded = raw.getBytes(StandardCharsets.US_ASCII);
        return new ByteArrayInputStream(usASCIIEncoded);
    }

}
