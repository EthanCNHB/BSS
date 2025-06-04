package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TeacherMapper;
import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Teacher;
import com.wang.bss.service.TeacherService;
import com.wang.bss.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Teacher findByUsername(String username) {
        Teacher t = teacherMapper.findByUsername(username);
        if (t == null) {
            throw new RuntimeException("教师不存在：" + username);
        }
        return t;
    }

    @Override
    public void register(String username, String password) {
        String md5 = Md5Util.getMD5String(password);
        teacherMapper.add(username, md5);
    }

    @Override
    public void assignCourse(Integer teacherId, Integer courseId) {
        teacherMapper.insertTeacherCourse(teacherId, courseId);
    }


    @Override
    public void update(Teacher teacher) {
        teacherMapper.update(teacher);
    }

    @Override
    public void updateAvatar(Integer userId, String avatarUrl) {
        teacherMapper.updateAvatar(userId, avatarUrl);
    }

    @Override
    public void updatePwd(Integer userId, String newPwd) {
        String md5 = Md5Util.getMD5String(newPwd);
        teacherMapper.updatePwd(userId, md5);
    }

    @Override
    public List<Course> findCoursesByTeacherId(Integer teacherId) {
        return teacherMapper.findCoursesByTeacherId(teacherId);
    }

    @Override
    public void unassignCourse(Integer teacherId, Integer courseId) {
        teacherMapper.deleteCourseAssignment(teacherId, courseId);
    }

    /**
     * 获取某教师授课课程，并加载教材信息
     */
    public List<Course> getCoursesWithTextbooks(Integer teacherId) {
        // 这里用 Mapper 先拿 Course，MyBatis 会自动给 courses 字段 lazy load
        List<Course> courses = teacherMapper.selectCoursesByTeacher(teacherId);
        // 触发加载教材集合
        courses.forEach(course -> course.getTextbooks().size());
        return courses;
    }
}
