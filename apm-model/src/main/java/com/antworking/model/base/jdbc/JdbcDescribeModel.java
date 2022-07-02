package com.antworking.model.base.jdbc;

import com.antworking.util.JsonUtil;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

/**
 * @author XiangXiaoWei
 * date 2022/6/15
 */
public class JdbcDescribeModel {


    // sql 语句
    private String sql;

    private String database;

    private String url;

    public List<ParamValues> params;


    public void putParams(ParamValues paramValues) {
        if (params==null) {
            params = new LinkedList<>();
        }
        this.params.add(paramValues);
    }

    public void reset() {
        this.sql=null;
        this.params=null;
    }

    public static class ParamValues {
        public ParamValues(int index, String value) {
            this.index = index;
            this.value = value;
        }

        public int index;
        public String value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParamValues> getParams() {
        return params;
    }

    public void setParams(List<ParamValues> params) {
        this.params = params;
    }


    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
