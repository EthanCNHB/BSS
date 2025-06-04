package com.wang.bss.service;

import com.wang.bss.pojo.Student;
import com.wang.bss.pojo.Course;

import java.util.List;

public interface StudentService {


    Student findByUsername(String username);

    void register(String username, String password);

    void update(Student student);

    void updatePwd(String newPwd);


    Student findById(Integer userId);

    /**
     * 查询所有学生列表
     * @return 全部 Student 对象集合
     */
    List<Student> findAll();

    // —— 多对多课程关联 —— //

    /** 查询某位学生的所有课程 */
    List<Course> findCoursesByStudentId(Integer studentId);

    /** 给学生分配课程 */
    void assignCourse(Integer studentId, Integer courseId);

    /** 解除学生与课程的绑定 */
    void unassignCourse(Integer studentId, Integer courseId);
}
