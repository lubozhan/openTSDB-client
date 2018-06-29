package org.opentsdb.client.exception;

import org.opentsdb.client.response.ResultResponse;

public class HttpServerNotSupportException extends HttpUnknowStatusException {

    private static final long serialVersionUID = 2049411279202620651L;

    public HttpServerNotSupportException(int status, String message) {
        super(status, message);
    }

    public HttpServerNotSupportException(ResultResponse result) {
        super(result);
    }

}
