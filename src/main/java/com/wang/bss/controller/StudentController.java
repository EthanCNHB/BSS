package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Student;
import com.wang.bss.pojo.Course;
import com.wang.bss.service.StudentService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    public static class RegisterRequest {
        @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须为5到16个非空白字符")
        public String username;

        @Pattern(regexp = "^\\S{5,16}$", message = "密码必须为5到16个非空白字符")
        public String password;
    }

    /** 注册新学生 */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest req) {
        Student exists = studentService.findByUsername(req.username);
        if (exists != null) {
            return Result.error("用户名已存在！");
        }
        studentService.register(req.username, req.password);
        return Result.success();
    }

    /** 获取当前学生信息 */
    @GetMapping("/studentInfo")
    public Result<Student> studentInfo() {
        String username = ensureAuthenticated();
        Student student = studentService.findByUsername(username);
        return Result.success(student);
    }

    /** 更新学生信息 */
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody Student student) {

        studentService.update(student);
        return Result.success();
    }

    /** 修改密码 */
    @PatchMapping("/updatePwd")
    public Result<Void> updatePwd(@RequestBody Map<String, String> params) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd  = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd)
                || !StringUtils.hasLength(newPwd)
                || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数！");
        }

        String username = ensureAuthenticated();
        Student loginStudent = studentService.findByUsername(username);

        if (!loginStudent.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误！");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次新密码不一致！");
        }

        studentService.updatePwd(newPwd);
        return Result.success();
    }

    /** 从 ThreadLocal 获取当前用户名，若未认证则抛异常 */
    private String ensureAuthenticated() {
        Object obj = ThreadLocalUtil.get();
        if (!(obj instanceof Map)) {
            throw new RuntimeException("未认证");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) obj;
        String username = (String) user.get("username");
        if (!StringUtils.hasText(username)) {
            throw new RuntimeException("未认证");
        }
        return username;
    }
    /** 根据 userId 查询单个学生信息（管理端使用） */
    @GetMapping("/{userId}")
    public Result<Student> getStudentById(@PathVariable Integer userId) {
        if (userId == null) {
            return Result.error("缺少 userId");
        }
        Student student = studentService.findById(userId);
        if (student == null) {
            return Result.error("未找到该学生");
        }
        return Result.success(student);
    }

    /** 查询所有学生列表（管理端使用） */
    @GetMapping("/list")
    public Result<List<Student>> listAllStudents() {
        List<Student> list = studentService.findAll();
        return Result.success(list);
    }

    /** 查询学生的所有课程 */
    @GetMapping("/{studentId}/courses")
    public Result<List<Course>> getCourses(@PathVariable Integer studentId) {
        ensureAuthenticated();
        List<Course> list = studentService.findCoursesByStudentId(studentId);
        return Result.success(list);
    }

    /** 给学生分配课程 */
    @PostMapping("/{studentId}/courses")
    public Result<Void> assignCourses(@PathVariable Integer studentId,
                                     @RequestBody List<Integer> courseIds) {
        ensureAuthenticated();
        if (courseIds == null || courseIds.isEmpty()) {
            return Result.error("请选择要分配的课程");
        }
        for (Integer courseId : courseIds) {
            studentService.assignCourse(studentId, courseId);
        }
        return Result.success();
    }

    /** 撤销学生的一门课程 */
    @DeleteMapping("/{studentId}/courses/{courseId}")
    public Result<Void> unassignCourse(@PathVariable Integer studentId,
                                       @PathVariable Integer courseId) {
        ensureAuthenticated();
        studentService.unassignCourse(studentId, courseId);
        return Result.success();
    }
}
