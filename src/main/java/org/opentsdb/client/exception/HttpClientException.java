package org.opentsdb.client.exception;

public class HttpClientException extends RuntimeException {


    private static final long serialVersionUID = -1849089684298200941L;

    public HttpClientException() {
    }

    public HttpClientException(Exception e) {
        super(e);
    }

    public HttpClientException(String messsage) {
        super(messsage);
    }

    public HttpClientException(String messsage,Exception e) {
        super(messsage,e);
    }

}
