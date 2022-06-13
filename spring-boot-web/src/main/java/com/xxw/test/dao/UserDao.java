package com.xxw.test.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from user where id = #{id}")
    User findUserById(String id);

    @Select("select * from user where id = #{id} and name =#{name}")
    User findUser(@Param("id")String id,@Param("name") String name);

    @Insert("insert into user (id,name,age) values (#{id},#{name},#{age})")
    int insertUser(@Param("id")String id,@Param("name")String name,@Param("age")String age);
}
