package org.opentsdb.client.exception;

import org.opentsdb.client.response.ResultResponse;

public class HttpServerErrorException extends HttpUnknowStatusException {

    private static final long serialVersionUID = 2542335849040762773L;

    public HttpServerErrorException(int status, String message) {
        super(status, message);
    }

    public HttpServerErrorException(ResultResponse result) {
        super(result);
    }

}
