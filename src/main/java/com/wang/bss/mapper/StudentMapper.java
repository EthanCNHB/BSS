package com.wang.bss.mapper;

import com.wang.bss.pojo.Student;
import com.wang.bss.pojo.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("select * from student where username=#{username}")
    Student findByUsername(String username);

    @Insert("INSERT INTO student (student_name, college_id, major_id, username, password, avatar) " +
            "VALUES (#{studentName}, #{collegeId}, #{majorId}, #{username}, #{password}, #{avatar})")
    void add(@Param("username") String username,
             @Param("password") String md5String,
             @Param("studentName") String studentName,
             @Param("collegeId") Integer collegeId,
             @Param("majorId") Integer majorId,
             @Param("avatar") String avatar);



    @Update("UPDATE student SET student_name=#{studentName}, college_id=#{collegeId}, " +
            "major_id=#{majorId}, avatar=#{avatar} WHERE user_id=#{userId}")
    void update(Student student);


    @Update("update student set password=#{md5String} where user_id=#{userId}")
    void updatePwd(String md5String, Integer userId);

    /**
     * 根据 userId 查询单个学生
     */
    @Select("SELECT user_id AS userId, username, student_name AS studentName, college_id AS collegeId, major_id AS majorId " +
            "FROM student WHERE user_id = #{userId}")
    Student selectById(@Param("userId") Integer userId);

    /**
     * 查询所有学生列表
     */
    @Select("SELECT user_id AS userId, username, student_name AS studentName, college_id AS collegeId, major_id AS majorId, email, phone " +
            "FROM student")
    List<Student> selectAll();

    // —— 多对多关联操作 —— //

    /** 查询某位学生的所有课程 */
    @Select("SELECT c.* FROM course c " +
            "JOIN student_course sc ON c.course_id = sc.course_id " +
            "WHERE sc.student_id = #{studentId}")
    List<Course> findCoursesByStudentId(@Param("studentId") Integer studentId);

    /** 插入学生-课程关联 */
    @Insert("INSERT INTO student_course (student_id, course_id) VALUES (#{studentId}, #{courseId})")
    void insertStudentCourse(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);

    /** 删除学生单条课程关联 */
    @Delete("DELETE FROM student_course WHERE student_id = #{studentId} AND course_id = #{courseId}")
    void deleteCourseAssignment(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);
}
