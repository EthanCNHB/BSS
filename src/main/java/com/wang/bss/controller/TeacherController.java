package com.wang.bss.controller;


import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Teacher;
import com.wang.bss.service.TeacherService;
import com.wang.bss.utils.JwtUtil;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,1000}$") String password) {
        Teacher t = teacherService.findByUserName(username);
        if (t == null) {
            teacherService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用！");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,1000}$") String password) {
        Teacher loginTeacher = teacherService.findByUserName(username);
        if (loginTeacher == null) {
            return Result.error("用户名错误！");
        }

        if (Md5Util.getMD5String(password).equals(loginTeacher.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", loginTeacher.getUserId());
            claims.put("username", loginTeacher.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误！");
    }

    //查询教师个人资料
    @GetMapping("/teacherInfo")
    public Result<Teacher> teacherInfo() {

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Teacher teacher = teacherService.findByUserName(username);
        return Result.success(teacher);

    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Teacher teacher) {
        teacherService.update(teacher);
        return Result.success();

    }

    @PatchMapping("updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        teacherService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params){
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd)||!StringUtils.hasLength(newPwd)||!StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数！");
        }

        //原密码是否正确
        //调用teacherService根据用户名拿到原密码，再和old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Teacher loginTeacher = teacherService.findByUserName(username);
        if (!loginTeacher.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误！");
        }

        //新旧密码是否一样
        if(!rePwd.equals(newPwd)){
            return Result.error("两次新密码填写的不一样！");
        }

        //2.调用Service完成密码更新

        teacherService.updatePwd(newPwd);
        return Result.success();

    }
}
