package com.wang.bss.mapper;

import com.wang.bss.pojo.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TeacherMapper {

    @Select("select * from teacher where username = #{username}")
    Teacher findByUserName(String username);

    @Insert("INSERT INTO teacher (username, password, college_id, major_id, teacher_name) " +
            "VALUES (#{username}, #{password}, '01', '01',  '教师名')")
    void add(String username, String password);

    @Update("update teacher set teacher_name=#{teacherName},college_id=#{collegeId}," +
            "major_id=#{majorId} where username = #{username}")
    void update(Teacher teacher);

    @Update("update teacher set teacher_pic=#{avatarUrl} where user_id = #{userId}")
    void updateAvatar(String avatarUrl, Integer userId);

    @Update("update teacher set password=#{md5String} where user_id=#{userId}")
    void updatePwd(String md5String, Integer userId);
}
