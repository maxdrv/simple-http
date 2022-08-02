package org.home;

import org.home.http.BadHttpVersionException;
import org.home.http.HttpVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

    @Test
    void getBestCompatibleVersionExactMatch() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
            assertNotNull(version);
            assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            fail(e);
        }
    }

    @Test
    void getBestCompatibleVersionBadFormat() {
        try {
            HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException e) {

        }
    }

    @Test
    void getBestCompatibleVersionHigherVersion() {
        try {
            HttpVersion version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            fail();
        }
    }

}
