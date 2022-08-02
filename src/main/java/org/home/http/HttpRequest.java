package org.home.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;  // literal from request
    private HttpVersion bestCompatibleVersion;

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }

    void setMethod(HttpMethod method) {
        this.method = method;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (method.name().equals(methodName)) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(HttpStatus.NOT_IMPLEMENTED);
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isBlank()) {
            throw new HttpParsingException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if (this.bestCompatibleVersion == null) {
            throw new HttpParsingException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
        }
    }
}
