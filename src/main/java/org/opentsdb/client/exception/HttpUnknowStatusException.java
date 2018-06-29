package org.opentsdb.client.exception;

import org.opentsdb.client.response.ResultResponse;

public class HttpUnknowStatusException extends RuntimeException {


    public HttpUnknowStatusException(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpUnknowStatusException(ResultResponse result) {
        super();
        this.status = result.getStatusCode();
        this.message = result.getContent();
    }

    private int status;

    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
