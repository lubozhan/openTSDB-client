package org.opentsdb.client.query.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import org.opentsdb.client.query.type.Aggregator;
import org.opentsdb.client.query.type.FilterType;

public class SubQuery {
    private int index;
    private String aggregator;

    // the field not need for json
    @JSONField(serialize=false)
    private Aggregator aggregatorType;

    private String metric;
    private String downsample;
    private Boolean rate;
    private Map<String, String> tags;
    private Boolean explicitTags;
    private Integer realTimeSeconds;
    private Integer limit;
    private Integer offset;
    private String dpValue;
    private List<Filter> filters;

    public static class Builder {
        @JSONField(deserialize=false)
        private Aggregator aggregatorType;
        private String metric;
        private String downsample;
        private Boolean rate;
        private Integer limit;
        private Integer offset;
        private String dpValue;
        private Map<String, String> tags = new HashMap<String, String>();
        private Boolean explicitTags;
        private Integer realTimeSeconds;
        private List<Filter> filters;

        public Builder(String metric, Aggregator aggregator) {
            this.metric = metric;
            this.aggregatorType = aggregator;
        }

        public Builder rate() {
            this.rate = true;
            return this;
        }

        public Builder limit() {
            this.limit = 0;
            return this;
        }

        public Builder offset() {
            this.offset = 0;
            return this;
        }

        public Builder dpValue() {
            this.dpValue = null;
            return this;
        }

        /**
         * add a filter
         * @param type FilterType
         * @param tagk tagkey
         * @param filter filter
         * @param groupBy grouyBy
         * @return Builder
         */
        public Builder filter(FilterType type, String tagk, String filter, Boolean groupBy) {
            if (filters == null) {
                filters = new ArrayList<Filter>();
            }

            Filter f = new Filter();
            f.setType(type);
            f.setFilter(filter);
            f.setTagk(tagk);
            if (true == groupBy) {
                f.setGroupBy(groupBy);
            }

            filters.add(f);
            return this;
        }

        /**
         * add a filter
         * @param filter filter
         * @return Builder
         */
        public Builder filter(Filter filter) {
            if (filters == null) {
                filters = new ArrayList<Filter>();
            }

            filters.add(filter);
            return this;
        }

        /**
         * add a filter
         * @param type tupe
         * @param tagk tagkey
         * @param filter filter
         * @return Builder
         */
        public Builder filter(FilterType type, String tagk, String filter) {
            this.filter(type, tagk, filter, null);
            return this;
        }

        /**
         * set the rate
         * @param rate rate
         * @return Builder
         */
        public Builder rate(boolean rate) {
            this.rate = rate;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder dpValue(String dpValue) {
            this.dpValue = dpValue;
            return this;
        }

        /**
         * set the downsample
         * @param downsample downsample
         * @return Builder
         */
        public Builder downsample(String downsample) {
            this.downsample = downsample;
            return this;
        }

        /**
         * add a tagkey and tagvalue
         * @param tagk tagkey
         * @param tagv tagvalue
         * @return Builder
         */
        public Builder tag(String tagk, String tagv) {
            this.tags.put(tagk, tagv);
            return this;
        }

        /**
         * add the tags
         * @param tags the map
         * @return Builder
         */
        public Builder tag(Map<String, String> tags) {
            this.tags.putAll(tags);
            return this;
        }

        public Builder explicitTags() {
            this.explicitTags = true;
            return this;
        }

        public Builder explicitTags(boolean explicitTags) {
            this.explicitTags = explicitTags;
            return this;
        }

        public Builder realtime(Integer secondes) {
            this.realTimeSeconds = secondes;
            return this;
        }

        public Builder realtime(long time, TimeUnit unit) {
            this.realTimeSeconds = (int) unit.toSeconds(time);
            return this;
        }

        public SubQuery build() {
            SubQuery subQuery = new SubQuery();
            subQuery.aggregatorType = this.aggregatorType;
            subQuery.aggregator = this.aggregatorType.getName();
            subQuery.downsample = this.downsample;
            subQuery.metric = this.metric;
            subQuery.tags = this.tags;
            subQuery.rate = this.rate;
            subQuery.realTimeSeconds = this.realTimeSeconds;
            subQuery.explicitTags = this.explicitTags;

            if (this.limit != null && this.limit > 0) {
                subQuery.limit = this.limit;
            }

            if (this.offset != null && this.offset > 0) {
                subQuery.offset = this.offset;
            }

            if (this.dpValue != null) {
                subQuery.dpValue = this.dpValue;
            }

            subQuery.filters = this.filters;
            if (subQuery.tags != null && subQuery.tags.isEmpty()) {
                subQuery.tags = null;
            }

            return subQuery;
        }
    }

    public static class MetricBuilder {
        private String metric;

        public MetricBuilder(String metric) {
            this.metric = metric;
        }

        public Builder aggregator(Aggregator aggregator) {
            return new Builder(metric, aggregator);
        }
    }

    public static class AggregatorBuilder {
        private Aggregator aggregator;

        public AggregatorBuilder(Aggregator aggregator) {
            this.aggregator = aggregator;
        }

        public Builder metric(String metric) {
            return new Builder(metric, aggregator);
        }
    }

    public static Builder metric(String metric, Aggregator aggregator) {
        Builder builder = new Builder(metric, aggregator);
        return builder;
    }

    public static MetricBuilder metric(String metric) {
        return new MetricBuilder(metric);
    }

    public static AggregatorBuilder aggregator(Aggregator aggregator) {
        return new AggregatorBuilder(aggregator);
    }

    public Aggregator getAggregatorType() {
        return aggregatorType;
    }

    public String getAggregator() {
        return aggregator;
    }

    public String getMetric() {
        return metric;
    }

    public String getDownsample() {
        return downsample;
    }

    public Boolean getRate() {
        return rate;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public Boolean isExplicitTags() {
        return explicitTags;
    }

    public Integer getRealTimeSeconds() {
        return realTimeSeconds;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getDpValue() {
        return dpValue;
    }
}

