package com.wang.bss.controller;

import com.wang.bss.pojo.CollegeAdmin;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.CollegeAdminService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collegeAdmin")
@Validated
public class CollegeAdminController {

    @Autowired
    private CollegeAdminService collegeAdminService;

    /**
     * 获取当前登录学院管理员信息
     * GET /collegeAdmin/me
     */
    @GetMapping("/me")
    public Result<CollegeAdmin> profile() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        if (username == null || username.isBlank()) {
            return Result.error("未认证");
        }
        CollegeAdmin admin = collegeAdminService.findByUsername(username);
        return Result.success(admin);
    }
    /**
     * 获取所有学院管理员列表
     * GET /collegeAdmin/list
     */
    @GetMapping("/list")
    public Result<List<CollegeAdmin>> list() {
        List<CollegeAdmin> admins = collegeAdminService.findAll();
        return Result.success(admins);
    }

    /**
     * 注册请求体，增加 collegeId 字段
     */
    public static class RegisterRequest {
        /** 学院 ID */
        public Integer collegeId;

        @Pattern(regexp = "^\\S{5,16}$", message = "用户名不合法")
        public String username;

        @Pattern(regexp = "^\\S{5,16}$", message = "密码不合法")
        public String password;
    }

    /**
     * 学院管理员注册
     * 直接调用 mapper.add，回写自增主键
     * 如果用户名已存在，则返回错误提示
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest req) {
        // 1. 检查用户名是否已被注册
        CollegeAdmin existing = collegeAdminService.findByUsername(req.username);
        if (existing != null) {
            return Result.error("用户名已被注册");
        }

        // 2. 构造实体
        CollegeAdmin admin = new CollegeAdmin();
        admin.setCollegeId(req.collegeId);
        admin.setUsername(req.username);
        admin.setPassword(req.password);

        // 3. 执行注册（插入并回写自增主键）
        collegeAdminService.register(admin);

        return Result.success();
    }

    /**
     * 更新当前学院管理员资料
     * PUT /collegeAdmin/update
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody @Valid CollegeAdmin admin) {
        collegeAdminService.update(admin);
        return Result.success();
    }

    /**
     * 修改密码
     * PATCH /collegeAdmin/password
     */
    public static class PwdRequest {
        @Pattern(regexp="^\\S{5,16}$", message="旧密码格式不正确")
        public String oldPwd;
        @Pattern(regexp="^\\S{5,16}$", message="新密码格式不正确")
        public String newPwd;
        @Pattern(regexp="^\\S{5,16}$", message="重复密码格式不正确")
        public String rePwd;
    }

    @PatchMapping("/password")
    public Result<Void> changePassword(@RequestBody @Valid PwdRequest p) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("userId");
        CollegeAdmin admin = collegeAdminService.findById(userId);
        if (!admin.getPassword().equals(Md5Util.getMD5String(p.oldPwd))) {
            return Result.error("原密码不正确");
        }
        if (!p.newPwd.equals(p.rePwd)) {
            return Result.error("两次新密码不一致");
        }
        collegeAdminService.updatePwd(userId, p.newPwd);
        return Result.success();
    }

    /**
     * 删除指定学院管理员
     * DELETE /collegeAdmin/{userId}
     */
    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable Integer userId) {
        collegeAdminService.deleteById(userId);
        return Result.success();
    }
}
