package com.wang.bss.mapper;

import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    /** 根据用户名查询教师 */
    @Select("SELECT * FROM teacher WHERE username = #{username}")
    Teacher findByUsername(@Param("username") String username);

    /** 注册教师（只传用户名和密码） */
    @Insert("INSERT INTO teacher (username, password) VALUES (#{username}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void add(@Param("username") String username,
             @Param("password") String password);

    /** 更新教师基本信息（name、college_id、major_id） */
    @Update("UPDATE teacher "
            + "SET teacher_name = #{teacherName}, college_id = #{collegeId}, major_id = #{majorId} "
            + "WHERE user_id = #{userId}")
    void update(Teacher teacher);

    /** 更新头像 */
    @Update("UPDATE teacher SET teacher_pic = #{avatarUrl} WHERE user_id = #{userId}")
    void updateAvatar(@Param("userId") Integer userId,
                      @Param("avatarUrl") String avatarUrl);

    /** 更新密码（传入已 MD5 处理后的字符串） */
    @Update("UPDATE teacher SET password = #{md5String} WHERE user_id = #{userId}")
    void updatePwd(@Param("userId") Integer userId,
                   @Param("md5String") String md5String);

    // —— 多对多 关联操作 —— //

    /** 查询某位教师的所有课程 */
    @Select("SELECT c.* FROM course c "
            + "JOIN teacher_course tc ON c.course_id = tc.course_id "
            + "WHERE tc.teacher_id = #{teacherId}")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Integer teacherId);

    /** 删除教师所有课程关联 */
    @Delete("DELETE FROM teacher_course WHERE teacher_id = #{teacherId}")
    void deleteCoursesByTeacherId(@Param("teacherId") Integer teacherId);

    /** 删除教师单条课程关联 */
    @Delete("DELETE FROM teacher_course "
            + "WHERE teacher_id = #{teacherId} AND course_id = #{courseId}")
    void deleteCourseAssignment(@Param("teacherId") Integer teacherId,
                                @Param("courseId") Integer courseId);

    /** 插入教师-课程关联 */
    @Insert("INSERT INTO teacher_course (teacher_id, course_id) VALUES (#{teacherId}, #{courseId})")
    void insertTeacherCourse(@Param("teacherId") Integer teacherId, @Param("courseId") Integer courseId);


    /**
     * 查教师所授课程（含教材列表）
     */
    @Select("""
    SELECT
      c.course_id,
      c.course_code,
      c.course_name,
      c.course_type
    FROM course c
      JOIN teacher_course tc ON c.course_id = tc.course_id
    WHERE tc.teacher_id = #{teacherId}
    """)
    @Results(id = "CourseWithTextbooks", value = {
            @Result(column = "course_id",   property = "courseId",   id = true),
            @Result(column = "course_code", property = "courseCode"),
            @Result(column = "course_name", property = "courseName"),
            @Result(column = "course_type", property = "courseType"),

            // 嵌套查询：把 course_id 传给下面这个方法，得到该课程的 textbook 列表
            @Result(property = "textbooks", column = "course_id",
                    javaType = List.class,
                    many = @Many(select = "com.wang.bss.mapper.TextbookMapper.findByCourseId"))
    })
    List<Course> selectCoursesByTeacher(@Param("teacherId") Integer teacherId);}
