package com.wang.bss.controller;

import com.wang.bss.mapper.AdministratorMapper;
import com.wang.bss.pojo.*;
import com.wang.bss.service.AdministratorService;
import com.wang.bss.service.CollegeAdminService;
import com.wang.bss.service.StudentService;
import com.wang.bss.service.TeacherService;
import com.wang.bss.utils.JwtUtil;
import com.wang.bss.utils.Md5Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CollegeAdminService CollegeAdminService;


    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String role = "";
        String token;

        // 1. 判断角色并验证用户名是否存在
        if (username.startsWith("ad")) {
            role = "admin";
            Admin admin = administratorService.findByUserName(username);
            if (admin == null) return Result.error("管理员用户名错误！");
            if (!password.equals(admin.getPassword())) return Result.error("密码错误！");
            token = JwtUtil.genToken(Map.of("userId", admin.getUserId(), "username", username, "role", role));
        } else if (username.startsWith("ca")) {
            role = "collegeAdmin";
            CollegeAdmin collegeAdmin = CollegeAdminService.findByUsername(username);
            if (collegeAdmin == null) return Result.error("学院管理员用户名错误！");
            if (!password.equals(collegeAdmin.getPassword())) return Result.error("密码错误！");
            token = JwtUtil.genToken(Map.of("userId", collegeAdmin.getUserId(), "username", username, "role", role));
        } else if (username.startsWith("tc")) {
            role = "teacher";
            Teacher teacher = teacherService.findByUsername(username);
            if (teacher == null) return Result.error("教师用户名错误！");
            if (!Md5Util.getMD5String(password).equals(teacher.getPassword())) return Result.error("密码错误！");
            token = JwtUtil.genToken(Map.of("userId", teacher.getUserId(), "username", username, "role", role));
        } else {
            role = "student";
            Student student = studentService.findByUsername(username);
            if (student == null) return Result.error("学生用户名错误！");
            if (!Md5Util.getMD5String(password).equals(student.getPassword())) return Result.error("密码错误！");
            token = JwtUtil.genToken(Map.of("userId", student.getUserId(), "username", username, "role", role));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("role", role);
        return Result.success(result);
    }
}

