package com.wang.bss.mapper;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class TextbookSqlProvider {

    public String findByConditions(Map<String, Object> conditions) {
        return new SQL() {{
            SELECT("*");
            FROM("textbook");
            if (conditions.get("name") != null) {
                WHERE("name = #{name}");
            }
            if (conditions.get("code") != null) {
                WHERE("code = #{code}");
            }
            if (conditions.get("publisher") != null) {
                WHERE("publisher = #{publisher}");
            }
            if (conditions.get("author") != null) {
                WHERE("author = #{author}");
            }
            if (conditions.get("price") != null) {
                WHERE("price = #{price}");
            }
            if (conditions.get("status") != null) {
                WHERE("status = #{status}");
            }
        }}.toString();
    }
}
