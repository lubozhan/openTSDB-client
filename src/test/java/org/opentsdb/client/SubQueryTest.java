package org.opentsdb.client;

import org.junit.Assert;
import org.junit.Test;
import org.opentsdb.client.query.request.Filter;
import org.opentsdb.client.query.request.Query;
import org.opentsdb.client.query.request.SubQuery;
import org.opentsdb.client.query.type.Aggregator;
import org.opentsdb.client.query.type.FilterType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubQueryTest {

    @Test
    public void testSubQuery() {
        SubQuery subQuery = SubQuery.metric("test1").aggregator(Aggregator.AVG).rate().downsample("hello").tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        String json = subQuery.toJSON();
        System.out.println(json);
        Assert.assertEquals(json,
                "{\"aggregator\":\"avg\",\"downsample\":\"hello\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}");
    }

    @Test
    public void testSubQueryTagMap() {
        Map<String,String> tagMap = new HashMap<String,String>();
        tagMap.put("tagk1", "tagv1");
        tagMap.put("tagk2", "tagv2");

        SubQuery subQuery = SubQuery
                .metric("test1")
                .aggregator(Aggregator.AVG)
                .rate().downsample("hello")
                .tag(tagMap)
                .build();

        String json = subQuery.toJSON();
        Assert.assertEquals(json,
                "{\"aggregator\":\"avg\",\"downsample\":\"hello\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}");
    }

    @Test
    public void testSubQueryFilter() {
        SubQuery subQuery = SubQuery
                .metric("test-metric")
                .aggregator(Aggregator.AVG)
                .rate()
                .downsample("60m-avg")
                .tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2")
                .filter(Filter.filter(FilterType.Regexp, "host", "web[0-9]+.lax.mysite.com",true).build())
                .filter(Filter.filter(FilterType.LiteralOr, "host2", "web[0-9]+.lax.mysite.com",true).build())
                .build();

        String json = subQuery.toJSON();
        Assert.assertEquals(json,
                "{\"aggregator\":\"avg\",\"downsample\":\"60m-avg\",\"filters\":[{\"filter\":\"web[0-9]+.lax.mysite.com\",\"groupBy\":true,\"tagk\":\"host\",\"type\":\"regexp\"},{\"filter\":\"web[0-9]+.lax.mysite.com\",\"groupBy\":true,\"tagk\":\"host2\",\"type\":\"literal_or\"}],\"index\":0,\"metric\":\"test-metric\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}");
    }

    @Test
    public void testQueryStartEnd() throws ParseException {
        String strDate = "2017-08-01 13:14:15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strDate);

        int endTime = (int) (date.getTime() / 1000);
        int startTime = endTime - 60 * 60;

        SubQuery subQuery1 = SubQuery.metric("test1").aggregator(Aggregator.SUM).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();
        SubQuery subQuery2 = SubQuery.metric("test2").aggregator(Aggregator.AVG).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        Query query = Query.start(startTime).end(endTime).sub(subQuery1).sub(subQuery2).build();
        String json = query.toJSON();
        Assert.assertEquals(json,
                "{\"end\":1501564455,\"queries\":[{\"aggregator\":\"sum\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"index\":1,\"metric\":\"test2\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}],\"start\":1501560855}");
    }

    @Test
    public void testQueryTimeRange() throws ParseException {
        String strDate = "2017-08-01 13:14:15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strDate);

        int endTime = (int) (date.getTime() / 1000);
        int startTime = endTime - 60 * 60;

        SubQuery subQuery1 = SubQuery.metric("test1").aggregator(Aggregator.SUM).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        SubQuery subQuery2 = SubQuery.metric("test2").aggregator(Aggregator.AVG).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        Query query = Query.timeRange(startTime, endTime)
                .sub(subQuery1).sub(subQuery2).build();

        String json = query.toJSON();
        Assert.assertEquals(json,
                "{\"end\":1501564455,\"queries\":[{\"aggregator\":\"sum\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"index\":1,\"metric\":\"test2\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}],\"start\":1501560855}");
    }

    @Test
    public void testQueryExplicitTags() throws ParseException {
        String strDate = "2017-08-01 13:14:15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strDate);

        int endTime = (int) (date.getTime() / 1000);
        int startTime = endTime - 60 * 60;

        SubQuery subQuery1 = SubQuery
                .metric("test1")
                .aggregator(Aggregator.SUM)
                .rate()
                .tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2")
                .explicitTags()
                .build();

        SubQuery subQuery2 = SubQuery
                .metric("test2")
                .aggregator(Aggregator.AVG)
                .rate()
                .tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2")
                .explicitTags(true)
                .build();

        SubQuery subQuery3 = SubQuery
                .metric("test3")
                .aggregator(Aggregator.AVG)
                .rate()
                .tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2")
                .build();

        Query query = Query.timeRange(startTime, endTime)
                .sub(subQuery1)
                .sub(subQuery2)
                .sub(subQuery3)
                .build();

        String json = query.toJSON();
        Assert.assertEquals(json,
                "{\"end\":1501564455,\"queries\":[{\"aggregator\":\"sum\",\"explicitTags\":true,\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"explicitTags\":true,\"index\":1,\"metric\":\"test2\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"index\":2,\"metric\":\"test3\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}],\"start\":1501560855}");
    }

    @Test
    public void testSubQueryRealtime() {
        SubQuery subQuery = SubQuery
                .metric("test1").aggregator(Aggregator.AVG).rate().downsample("none")
                .tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2")
                .realtime(100)
                .build();

        String json = subQuery.toJSON();
        Assert.assertEquals(json,
                "{\"aggregator\":\"avg\",\"downsample\":\"none\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"realTimeSeconds\":100,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}");
    }

    @Test
    public void testQueryDelete() throws ParseException {
        String strDate = "2017-08-01 13:14:15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strDate);

        int endTime = (int) (date.getTime() / 1000);
        int startTime = endTime - 60 * 60;

        SubQuery subQuery1 = SubQuery.metric("test1").aggregator(Aggregator.SUM).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();
        SubQuery subQuery2 = SubQuery.metric("test2").aggregator(Aggregator.AVG).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        Query query = Query.start(startTime).end(endTime)
                .sub(subQuery1).sub(subQuery2)
                .delete()
                .build();

        String json = query.toJSON();
        Assert.assertEquals(json,
                "{\"delete\":true,\"end\":1501564455,\"queries\":[{\"aggregator\":\"sum\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"index\":1,\"metric\":\"test2\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}],\"start\":1501560855}");
    }

    @Test
    public void testQueryMsResolution() throws ParseException {
        String strDate = "2017-08-01 13:14:15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(strDate);

        int endTime = (int) (date.getTime() / 1000);
        int startTime = endTime - 60 * 60;

        SubQuery subQuery1 = SubQuery.metric("test1").aggregator(Aggregator.SUM).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        SubQuery subQuery2 = SubQuery.metric("test2").aggregator(Aggregator.AVG).rate().tag("tagk1", "tagv1")
                .tag("tagk2", "tagv2").build();

        Query query = Query.timeRange(startTime, endTime)
                .msResolution(true)
                .sub(subQuery1).sub(subQuery2).build();

        String json = query.toJSON();
        Assert.assertEquals(json,
                "{\"end\":1501564455,\"msResolution\":true,\"queries\":[{\"aggregator\":\"sum\",\"index\":0,\"metric\":\"test1\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}},{\"aggregator\":\"avg\",\"index\":1,\"metric\":\"test2\",\"rate\":true,\"tags\":{\"tagk1\":\"tagv1\",\"tagk2\":\"tagv2\"}}],\"start\":1501560855}");
    }
}