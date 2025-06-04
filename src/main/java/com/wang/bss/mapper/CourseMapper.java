package com.wang.bss.mapper;

import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Textbook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 获取所有课程
    @Select("SELECT * FROM course")
    List<Course> getAllCourses();

    // 添加课程
    @Insert("INSERT INTO course(course_code, course_name, course_type, credit, description,major_id) " +
            "VALUES(#{courseCode}, #{courseName}, #{courseType}, #{credit}, #{description},#{majorId})")
    void addCourse(Course course);

    // 更新课程
    @Update("UPDATE course SET course_code=#{courseCode}, course_name=#{courseName}, course_type=#{courseType}, " +
            "credit=#{credit}, description=#{description},major_id=#{majorId} WHERE course_id=#{courseId}")
    void updateCourse(Course course);

    // 删除课程
    @Delete("DELETE FROM course WHERE course_id=#{courseId}")
    void deleteCourse(Integer courseId);

    // 根据 ID 查询课程
    @Select("SELECT * FROM course WHERE course_id=#{courseId}")
    Course getCourseById(Integer courseId);

    @Select("SELECT * FROM course WHERE major_id=#{majorId}")
    List<Course> getCourseByMajorId(Integer majorId);

}
