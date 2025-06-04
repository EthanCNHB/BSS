package com.wang.bss.controller;


import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public Result<List<Course>> getAllCourses() {
        return Result.success(courseService.getAll());
    }

    @PostMapping("/add")
    public Result<Void> addCourse(@RequestBody Course course) {
        courseService.add(course);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody Course course) {
        courseService.update(course);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Integer id) {
        Course course = courseService.getById(id);
        return course != null ? Result.success(course) : Result.error("未找到该课程");
    }

    @GetMapping("/majorCourses/{majorId}")
    public  Result<List<Course>> getByMajorId(@PathVariable Integer majorId) {
        List<Course> courses = courseService.getByMajorId(majorId); // 这应该返回课程列表
        return Result.success(courses);
    }
}

