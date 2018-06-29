package org.opentsdb.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

public class HttpClient {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private CloseableHttpClient httpClient;

    public HttpClient() {
        HttpClientBuilder Hbuilder = HttpClientBuilder.create();
        httpClient = Hbuilder.build();
    }

    public HttpResponse PostData(String url, String json) throws IOException
    {
        StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost request = new HttpPost(url);
        request.setEntity(requestEntity);
        return httpClient.execute(request);
    }
    public void shutdown() throws IOException {
        httpClient.close();
    }
}