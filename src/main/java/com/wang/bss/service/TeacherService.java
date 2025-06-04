
package com.wang.bss.service;

import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Teacher;

import java.util.List;

public interface TeacherService {

    /**
     * 根据用户名查询教师，找不到时抛异常
     */
    Teacher findByUsername(String username);

    void register(String username, String password);

    void update(Teacher teacher);

    void updateAvatar(Integer userId, String avatarUrl);

    void updatePwd(Integer userId, String newPwd);

    // 多对多课程关联
    List<Course> findCoursesByTeacherId(Integer teacherId);

    void unassignCourse(Integer teacherId, Integer courseId);

    void assignCourse(Integer teacherId, Integer courseId);

    List<Course> getCoursesWithTextbooks(Integer teacherId);

    }
