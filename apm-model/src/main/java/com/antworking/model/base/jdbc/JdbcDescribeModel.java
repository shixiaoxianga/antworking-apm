package com.antworking.model.base.jdbc;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

/**
* @author XiangXiaoWei
* date 2022/6/15
*/
public class JdbcDescribeModel {


    private List<JdbcNode> nodes;

    public String transactional;

    public boolean autoCommit;

    public static class JdbcNode{

        // jdbc url
        private String jdbcUrl;
        // sql 语句
        private String sql;
        // 数据库名称
        private String databaseName;
        // 处理类型 pre：预处理 存储过程cell 其它：other
        private String statement;

        public List<ParamValues> params;


        public static class ParamValues{
            public ParamValues(int index, String value) {
                this.index = index;
                this.value = value;
            }

            public int index;
            public String value;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public String getStatement() {
            return statement;
        }

        public void setStatement(String statement) {
            this.statement = statement;
        }

        public List<ParamValues> getParams() {
            if(params==null)params = new LinkedList<>();
            return params;
        }

        public void setParams(List<ParamValues> params) {
            this.params = params;
        }
        public void putParams(ParamValues param) {
            if(params==null)params = new LinkedList<>();
            this.params.add(param);
        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public List<JdbcNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<JdbcNode> nodes) {
        this.nodes = nodes;
    }
    public void putNodes(JdbcNode node) {
        if(this.nodes==null){
            this.nodes=new LinkedList<>();
        }
        this.nodes.add(node);
    }

    public String getTransactional() {
        return transactional;
    }

    public void setTransactional(String transactional) {
        this.transactional = transactional;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
