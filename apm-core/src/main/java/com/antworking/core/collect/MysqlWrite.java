package com.antworking.core.collect;

import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.core.tools.JdbcUtil;
import com.antworking.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class MysqlWrite implements IWrite {
    private static final AwLog log = LoggerFactory.getLogger(MysqlWrite.class);

    private final IWrite write;

//    private final static Connection connection;
//    static {
//        try {
//            connection = JdbcUtil.jdbcHelper.getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public MysqlWrite(IWrite write) {
        this.write = write;
    }

    @Override
    public Integer order() {
        return 0;
    }

    @Override
    public void write(List<CollectDataBaseModel> models) {
        if (isWriteEnable()) {
            StringBuffer sql = new StringBuffer("insert into `apm_logs` " +
                    "(`app_node`, `begin_time`, `data`, `end_time`, `error`, `id`, `is_web`, " +
                    "`order`, `thread_name`, `trace_id`, `use_time`) values ");
            List<Object> args = new ArrayList<>();
            for (CollectDataBaseModel model : models) {
                sql.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ,");

                args.add(JsonUtil.toJsonString(model.getAppNode()));
                args.add(model.getBeginTime());
                args.add(JsonUtil.toJsonString(model.getData()));
                args.add(model.getEndTime());
                args.add(JsonUtil.toJsonString(model.getError()));
                args.add(model.getId());
                args.add(model.getWeb());
                args.add(model.getOrder());
                args.add(model.getThreadName());
                args.add(model.getTraceId());
                args.add(model.getUseTime());

            }



            JdbcUtil.jdbcHelper.executeUpdate(
                    sql.substring(0,sql.toString().lastIndexOf(",")),
                    args.toArray()
            );
            if (write != null) {
                write.write(models);
            }
        }
    }

    @Override
    public boolean isWriteEnable() {
        return true;
    }
}
