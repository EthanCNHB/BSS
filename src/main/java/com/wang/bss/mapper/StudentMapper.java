package com.wang.bss.mapper;

import com.wang.bss.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentMapper {

    @Select("select * from student where username=#{username}")
    Student findByUsername(String username);

    @Insert("insert into student (student_name, college_id, major_id,  username, password)" +
            "VALUE ('学生名','1','1',#{username},#{md5String})")
    void add(String username, String md5String);

    @Update("update student set student_name=#{studentName},college_id=#{collegeId}," +
            "major_id=#{majorId} where username = #{username}")
    void update(Student student);

    @Update("update student set password=#{md5String} where user_id=#{userId}")
    void updatePwd(String md5String, Integer userId);
}
