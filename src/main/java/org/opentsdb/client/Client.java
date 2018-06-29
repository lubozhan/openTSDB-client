package org.opentsdb.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.exception.HttpUnknowStatusException;
import org.opentsdb.client.query.request.Query;
import org.opentsdb.client.query.response.QueryResult;
import org.opentsdb.client.response.ErrorOrSuccDetail;
import org.opentsdb.client.response.HttpRequest;

public interface Client {

    /**
     * Process Query
     * @param query query
     * @return Query result : List
     */
    List<QueryResult> query(Query query) throws HttpUnknowStatusException, IOException;

    /**
	 * Sends metrics from the builder to the TSDB server.
	 *
	 * @param builder metrics builder
	 * @return result : List from the server
	 * @throws URISyntaxException IOException problem occurred sending to the server
	 */
    ErrorOrSuccDetail pushMetrics(MetricBuilder builder) throws URISyntaxException,IOException;

    /**
     * Sends metrics from the builder to the TSDB server.
     *
     * @param builder metrics builder
     * @param exceptResponse request type
     * @return result : List from the server
     * @throws URISyntaxException IOException problem occurred sending to the server
     */
    ErrorOrSuccDetail pushMetrics(MetricBuilder builder,
                            HttpRequest exceptResponse) throws URISyntaxException, IOException;

    /**
     * Shuts down the client. Should be called when done using the client.
     *
     * @throws IOException if could not shutdown the client
     */
    void shutdown() throws IOException;
}