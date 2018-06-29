package org.opentsdb.client.query.response;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class QueryResult {
    private String metric;
    private Map<String, String> tags;
    private List<String> aggregateTags;
    private LinkedHashMap<Long, Object> dps = new LinkedHashMap<Long, Object>();
    private LinkedHashMap<Long, String> sdps = new LinkedHashMap<Long, String>();

    public List<String> getAggregateTags() {
        return aggregateTags;
    }

    public void setAggregateTags(List<String> aggregateTags) {
        this.aggregateTags = aggregateTags;
    }

    public LinkedHashMap<Long, Object> getDps() {
        return dps;
    }

    public String getMetric() {
        return metric;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setDps(LinkedHashMap<Long, Object> dps) {
        this.dps = dps;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public LinkedHashMap<Long, String> getSdps() {
        return sdps;
    }

    public void setSdps(LinkedHashMap<Long, String> sdps) {
        this.sdps = sdps;
    }

}

