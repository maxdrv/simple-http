package org.home.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String literal;
    public final int major;
    public final int minor;

    HttpVersion(String literal, int major, int minor) {
        this.literal = literal;
        this.major = major;
        this.minor = minor;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+)\\.(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if (!matcher.find() || matcher.groupCount() != 2) {
            throw new BadHttpVersionException();
        }
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion bestCompatible = null;
        for (HttpVersion version : values()) {
            if (version.literal.equals(literalVersion)) {
                return version;
            } else {
                if (version.major == major) {
                    if (version.minor < minor) {
                        bestCompatible = version;
                    }
                }
            }
        }
        return bestCompatible;
    }

}
