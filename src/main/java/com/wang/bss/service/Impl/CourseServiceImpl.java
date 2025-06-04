package com.wang.bss.service.Impl;

import com.wang.bss.pojo.Course;
import com.wang.bss.service.CourseService;
import com.wang.bss.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getAll() {
        return courseMapper.getAllCourses();
    }

    @Override
    public void add(Course course) {
        courseMapper.addCourse(course);
    }

    @Override
    public void update(Course course) {
        courseMapper.updateCourse(course);
    }

    @Override
    public void delete(Integer id) {
        courseMapper.deleteCourse(id);
    }

    @Override
    public Course getById(Integer id) {
        return courseMapper.getCourseById(id);
    }

    @Override
    public List<Course> getByMajorId(Integer majorId) {
        return courseMapper.getCourseByMajorId(majorId);
    }
}
