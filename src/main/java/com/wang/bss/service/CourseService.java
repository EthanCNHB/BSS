package com.wang.bss.service;

import com.wang.bss.pojo.Course;

import java.util.List;

public interface CourseService {

    // 获取所有课程
    List<Course> getAll();

    // 添加课程
    void add(Course course);

    // 更新课程
    void update(Course course);

    // 删除课程
    void delete(Integer id);

    // 根据 ID 查询课程
    Course getById(Integer id);

    List<Course> getByMajorId(Integer majorId);
}
