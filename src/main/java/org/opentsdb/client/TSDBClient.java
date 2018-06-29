package org.opentsdb.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.alibaba.fastjson.JSON;

import org.apache.http.HttpResponse;
import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.exception.HttpServerErrorException;
import org.opentsdb.client.exception.HttpServerNotSupportException;
import org.opentsdb.client.exception.HttpUnknowStatusException;
import org.opentsdb.client.response.ErrorOrSuccDetail;
import org.opentsdb.client.response.HttpRequest;
import org.opentsdb.client.response.HttpStatus;
import org.opentsdb.client.response.ResultResponse;
import org.opentsdb.client.query.request.Query;
import org.opentsdb.client.query.response.QueryResult;

/**
 * HTTP implementation.
 */
public class TSDBClient implements Client {
    public final static String PUT = "/api/put";
    public final static String QUERY = "/api/query?";
    // bellow api not implemented
    public final static String QUERY_LAST = "/api/query/last";
    public final static String SUGGEST = "/api/suggest";
    public final static String LOOKUP = "/api/search/lookup";

	private String Url;
	private HttpClient httpClient;

	public TSDBClient(String Url) throws MalformedURLException
	{
		this.Url = checkNotNull(Url);
		new URL(Url); //validate url
		httpClient = new HttpClient();
	}

	@Override
    public List<QueryResult> query(Query query) throws IOException
    {
        HttpResponse response  = httpClient.PostData(Url + QUERY, JSON.toJSONString(query));
        return getQueryResponse(response);
    }

	@Override
	public ErrorOrSuccDetail pushMetrics(MetricBuilder builder) throws IOException {
		return pushMetrics(builder, HttpRequest.NONE);
	}

	@Override
	public ErrorOrSuccDetail pushMetrics(MetricBuilder builder,
			HttpRequest expectResponse) throws IOException {
		checkNotNull(builder);
		String url = builPutdUrl(Url, expectResponse);
        HttpResponse response =  httpClient.PostData(url, builder.build());
		return getPutAndResponseDetail(response);
	}

	private String builPutdUrl(String Url, HttpRequest expectResponse)
    {
		String url = Url + PUT;
		switch (expectResponse) {
		case SUMMARY:
			url += "?summary";
			break;
		case DETAIL:
			url += "?details";
			break;
		default:
			break;
		}

		return url;
	}

	//The Response from query request.
	private List<QueryResult> getQueryResponse(HttpResponse httpResponse)
    {
        ResultResponse resultResponse = ResultResponse.simplify(httpResponse);
        HttpStatus httpStatus = resultResponse.getHttpStatus();
        switch (httpStatus) {
            case ServerSuccessNoContent:
                return null;
            case ServerSuccess:
                String content = resultResponse.getContent();
                List<QueryResult> queryResultList;
                queryResultList = JSON.parseArray(content, QueryResult.class);
                return queryResultList;
            case ServerNotSupport:
                throw new HttpServerNotSupportException(resultResponse);
            case ServerError:
                throw new HttpServerErrorException(resultResponse);
            default:
                throw new HttpUnknowStatusException(resultResponse);
        }
	}

	// The response from put request
	private ErrorOrSuccDetail getPutAndResponseDetail(HttpResponse httpResponse)
    {
        ResultResponse resultResponse = ResultResponse.simplify(httpResponse);
        HttpStatus httpStatus = resultResponse.getHttpStatus();
        switch (httpStatus) {
            case ServerSuccessNoContent:
            case ServerSuccess:
                String content = resultResponse.getContent();
                ErrorOrSuccDetail detail = JSON.parseObject(content, ErrorOrSuccDetail.class);
                return detail;
            case ServerNotSupport:
                throw new HttpServerNotSupportException(resultResponse);
            case ServerError:
                throw new HttpServerErrorException(resultResponse);
            default:
                throw new HttpUnknowStatusException(resultResponse);
        }

    }
    @Override
    public void shutdown() throws IOException
    {
        httpClient.shutdown();
    }
}