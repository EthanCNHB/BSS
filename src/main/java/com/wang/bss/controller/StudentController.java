package com.wang.bss.controller;

import com.wang.bss.pojo.LoginRequest;
import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Student;
import com.wang.bss.service.StudentService;
import com.wang.bss.utils.JwtUtil;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    //添加学生
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        Student std = studentService.findByUsername(username);
        if (std == null) {
            studentService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已存在！");
        }
    }

    //学生登录
    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Student loginstd = studentService.findByUsername(username);

        if (loginstd == null) {
            return Result.error("用户名错误！");
        }

        if (Md5Util.getMD5String(password).equals(loginstd.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", loginstd.getUserId());
            claims.put("username", loginstd.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误！");
    }

    //查询学生个人资料
    @GetMapping("/studentInfo")
    public Result<Student> studentInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        // 检查用户是否已认证
        if (username == null) {
            return Result.error("未认证"); // 返回未认证状态
        }

        // 查找学生信息
        Student student = studentService.findByUsername(username);
        return Result.success(student);
    }


    //更改学生资料
    @PutMapping("/update")
    public Result update(@RequestBody @Validated Student student) {
        studentService.update(student);
        return Result.success();

    }

    //更改学生密码
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数！");
        }

        //原密码是否正确
        //调用teacherService根据用户名拿到原密码，再和old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Student loginTeacher = studentService.findByUsername(username);
        if (!loginTeacher.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误！");
        }

        //新旧密码是否一样
        if (!rePwd.equals(newPwd)) {
            return Result.error("两次新密码填写的不一样！");
        }

        //2.调用Service完成密码更新

        studentService.updatePwd(newPwd);
        return Result.success();

    }
}
