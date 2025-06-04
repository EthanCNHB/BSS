package com.wang.bss.mapper;

import com.wang.bss.pojo.CollegeAdmin;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 学院管理员 Mapper
 */
@Mapper
public interface CollegeAdminMapper {

    @Select("SELECT * FROM college_admin WHERE username = #{username}")
    CollegeAdmin findByUsername(@Param("username") String username);

    @Select("SELECT * FROM college_admin WHERE user_id = #{userId}")
    CollegeAdmin findById(@Param("userId") Integer userId);

    /**
     * 更新 除主键外的所有字段
     */
    @Update("update college_admin set college_id=#{collegeId},username=#{username} where user_id=#{userId}")
    void update(CollegeAdmin admin);

    /**
     * 仅更新密码
     */
    @Update("UPDATE college_admin SET password = #{md5Pwd} WHERE user_id = #{userId}")
    void updatePwd(@Param("userId") Integer userId, @Param("md5Pwd") String md5Pwd);

    /**
     * 插入除主键外的所有字段，并回写自增主键
     */
    @Insert("""
        INSERT INTO college_admin (college_id, username, password)
        VALUES (#{collegeId}, #{username}, #{password})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void add(CollegeAdmin admin);

    @Select("SELECT * FROM college_admin")
    List<CollegeAdmin> findAll();

    @Delete("DELETE FROM college_admin WHERE user_id = #{userId}")
    void deleteById(@Param("userId") Integer userId);
}
