package com.antworking;

import com.antworking.core.collect.MysqlWrite;
import com.antworking.core.config.AwConfigManager;
import com.antworking.model.collect.CollectDataBaseModel;

import java.util.ArrayList;

public class MysqlTest {

    public static void main(String[] args) {
        AwConfigManager.initConfig();
//        System.out.println(JdbcUtil.jdbcHelper);
//        JdbcUtil.jdbcHelper.executeUpdate("insert into `apm_logs` " +
//                        "(`app_node`, `begin_time`, `data`, `end_time`, `error`, `id`, `is_web`, `order`, `thread_name`, `trace_id`, `use_time`)" +
//                        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1",
//                "1"
//        );

        ArrayList<CollectDataBaseModel> models = new ArrayList<>();
        models.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));
        models.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));
        new MysqlWrite(null).write(models);
        ArrayList<CollectDataBaseModel> models2 = new ArrayList<>();
        models2.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));
        models2.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));

        new MysqlWrite(null).write(models2);

        ArrayList<CollectDataBaseModel> models3 = new ArrayList<>();
        models3.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));
        models3.add(CollectDataBaseModel.init(true,null,null,"thread","111111"));

        new MysqlWrite(null).write(models3);
    }

}
