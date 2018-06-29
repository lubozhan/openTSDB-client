package org.opentsdb.client.query.type;

import java.lang.reflect.Field;

public enum FilterType {
    LiteralOr("literal_or"), NotLiteralOr("not_literal_or"), Wildcard("wildcard"), Regexp("regexp");

    private String name;

    private FilterType(String name) {
        this.name = name;
        Class<?> superclass = this.getClass().getSuperclass();
        try {
            Field field = superclass.getDeclaredField("name");
            field.setAccessible(true);
            field.set(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
