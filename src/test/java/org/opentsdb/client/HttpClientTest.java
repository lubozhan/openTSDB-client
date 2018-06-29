package org.opentsdb.client;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.query.request.Query;
import org.opentsdb.client.query.request.SubQuery;
import org.opentsdb.client.query.response.QueryResult;
import org.opentsdb.client.query.type.Aggregator;
import org.opentsdb.client.response.ErrorOrSuccDetail;
import org.opentsdb.client.response.HttpRequest;

public class HttpClientTest {

	@Test
	public void test_pushMetrics_DefaultRetries() throws IOException{
		TSDBClient client = new TSDBClient("http://localhost:4242");

		MetricBuilder builder = MetricBuilder.getInstance();

		builder.addMetric("metric1").addDataPoint(1530247288, 30L)
				.addTag("tag1", "tab1value").addTag("tag2", "tab2value");

		builder.addMetric("metric2").addDataPoint(1530247289,44)
				.addTag("tag1", "tab1value");

		try {
            ErrorOrSuccDetail result= client.pushMetrics(builder,
                    HttpRequest.DETAIL);
            System.out.println("查询结果：" + result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.shutdown();
		}
	}


    @Test
    public void testQuery() throws IOException{
        TSDBClient client = new TSDBClient("http://localhost:4242");

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date startTime = calendar.getTime();

        Query query = Query.timeRange(startTime, now).sub(
                SubQuery.metric("metric1")
                        .aggregator(Aggregator.SUM)
                        .tag("tag1", "tab1value")
                        .build()
        ).sub(SubQuery.metric("test1").aggregator(Aggregator.AVG).rate().downsample("none")
                        .tag("tagk1", "tagv1")
                        .tag("tagk2", "tagv2")
                        .realtime(100)
                        .build()
        ).build();

        try {
            List<QueryResult> result = client.query(query);
            System.out.println("查询结果：" + result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}