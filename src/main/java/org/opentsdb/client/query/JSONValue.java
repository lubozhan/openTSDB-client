package org.opentsdb.client.query;

import com.alibaba.fastjson.JSON;

public class JSONValue {

    public static <T extends JSONValue> T parseObject(String json, Class<T> clazz) {
        T object = JSON.parseObject(json, clazz);
        return object;
    }

    public String toJSON() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public void appendJSON(final StringBuilder sb) {
        sb.append(toJSON());
    }

}

