A Simple OpenTSDB Client
====================

The OpenTSDB client is a Java library that simply implements the api for sending metrics and querying the OpenTSDB server.

**NOTE** This project only implements the synchronized http api for /api/put? and /api/query, please feel free to fork and push a implementation pr 
for other api if you like.

## Sending Metrics

Sending metrics is done by using the MetricBuilder. You simply add a metric, the tags associated with the metric, and
the data points.

    TSDBClient client = new TSDBClient("http://localhost:4242");

    MetricBuilder builder = MetricBuilder.getInstance();

    builder.addMetric("metric1").addDataPoint(1530247288, 30L)
            .addTag("tag1", "tab1value").addTag("tag2", "tab2value");

    builder.addMetric("metric2").addDataPoint(1530247289,44)
            .addTag("tag1", "tab1value");

    try {
        ErrorOrSuccDetail result= client.pushMetrics(builder,
                HttpRequest.DETAIL);
        System.out.println("result is：" + result);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        client.shutdown();
    }

## Query Data Point
Probably the most useful endpoint in the API, /api/query enables extracting data from the storage system in various 
formats determined by the serializer selected. Queries can be submitted via the 1.0 query string format or body content.
You can simply use the below api to query data.

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
        System.out.println("result is：" + result);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        client.shutdown();
    }

## Copyright and License

Copyright 2018 Proofpoint Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.