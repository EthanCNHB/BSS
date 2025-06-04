package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Course;
import com.wang.bss.pojo.Teacher;
import com.wang.bss.service.TeacherService;
import com.wang.bss.service.TextbookService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 教师控制层，处理登录、注册、信息更新及课程关联等操作。
 */
@RestController
@RequestMapping("/teacher")
@Validated
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TextbookService textbookService;

    /**
     * 登录请求体
     */
    public static class LoginRequest {
        @Pattern(regexp = "^\\S{5,16}$", message = "用户名不合法")
        public String username;

        @Pattern(regexp = "^\\S{5,16}$", message = "密码不合法")
        public String password;
    }


    /**
     * 注册请求体
     */
    public static class RegisterRequest {
        @Pattern(regexp = "^\\S{5,16}$", message = "用户名不合法")
        public String username;

        @Pattern(regexp = "^\\S{5,16}$", message = "密码不合法")
        public String password;
    }

    /**
     * 教师注册
     *
     * @param req 注册请求，包含用户名和密码
     * @return 无
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest req) {
        teacherService.register(req.username, req.password);
        return Result.success();
    }

    /**
     * 获取当前登录教师信息
     *
     * @return 教师实体
     */
    @GetMapping("/me")
    public Result<Teacher> profile() {
        String username = getAuthUsername();
        Teacher t = teacherService.findByUsername(username);
        return Result.success(t);
    }

    /**
     * 更新当前登录教师基本信息
     *
     * @param teacher 教师信息实体
     * @return 无
     */
    @PutMapping("/update")
    public Result<Void> update(@Valid @RequestBody Teacher teacher) {
        // teacher.setUserId(getAuthUserId());
        teacherService.update(teacher);
        return Result.success();
    }

    /**
     * 更新教师头像
     *
     * @param avatarUrl 头像 URL
     * @return 无
     */
    @PatchMapping("/avatar")
    public Result<Void> updateAvatar(@RequestParam String avatarUrl) {
        Integer uid = getAuthUserId();
        teacherService.updateAvatar(uid, avatarUrl);
        return Result.success();
    }

    /**
     * 修改密码请求体
     */
    public static class PwdRequest {
        @Pattern(regexp = "^\\S{5,16}$", message = "原密码不合法")
        public String oldPwd;

        @Pattern(regexp = "^\\S{5,16}$", message = "新密码不合法")
        public String newPwd;

        @Pattern(regexp = "^\\S{5,16}$", message = "重复密码不合法")
        public String rePwd;
    }

    /**
     * 修改登录密码
     *
     * @param p 密码请求体，包含原密码、新密码和确认密码
     * @return 无
     */
    @PatchMapping("/password")
    public Result<Void> updatePwd(@Valid @RequestBody PwdRequest p) {
        String username = getAuthUsername();
        Teacher t = teacherService.findByUsername(username);
        if (!t.getPassword().equals(Md5Util.getMD5String(p.oldPwd))) {
            return Result.error("原密码错误");
        }
        if (!p.newPwd.equals(p.rePwd)) {
            return Result.error("两次新密码不一致");
        }
        teacherService.updatePwd(t.getUserId(), p.newPwd);
        return Result.success();
    }

    /**
     * 查询某位教师所授的【基础】课程（不含教材信息）
     *
     * @param teacherId 教师ID
     * @return 课程列表
     */
    @GetMapping("/{teacherId}/courses")
    public Result<List<Course>> getCoursesBasic(
            @PathVariable("teacherId") Integer teacherId) {
        ensureAuthenticated();
        List<Course> list = teacherService.findCoursesByTeacherId(teacherId);
        return Result.success(list);
    }

    /**
     * 查询某位教师所授的课程【含教材信息】
     *
     * @param teacherId 教师ID
     * @return 带 textbooks 集合的课程列表
     */
    @GetMapping("/{teacherId}/courses-with-textbooks")
    public Result<List<Course>> getCoursesWithTextbooks(
            @PathVariable("teacherId") Integer teacherId) {
        ensureAuthenticated();
        List<Course> list = teacherService.getCoursesWithTextbooks(teacherId);
        return Result.success(list);
    }

    /**
     * 给教师分配课程
     *
     * @param teacherId 教师ID
     * @param courseIds 课程ID列表
     * @return 无
     */
    @PostMapping("/{teacherId}/courses")
    public Result<Void> assignCourses(
            @PathVariable Integer teacherId,
            @RequestBody List<Integer> courseIds) {
        ensureAuthenticated();

        // 如果 courseIds 为空，则返回错误提示
        if (courseIds == null || courseIds.isEmpty()) {
            return Result.error("请选择要分配的课程");
        }

        // 逐个插入课程
        for (Integer courseId : courseIds) {
            // 调用服务层方法逐个插入
            teacherService.assignCourse(teacherId, courseId);
        }

        return Result.success();
    }

    // —— 辅助方法 —— //

    /**
     * 确保已登录并返回用户名
     *
     * @return 当前登录用户名
     */
    private String getAuthUsername() {
        @SuppressWarnings("unchecked")
        Map<String, Object> claims = (Map<String, Object>) ThreadLocalUtil.get();
        String u = (String) claims.get("username");
        if (u == null || u.isBlank()) {
            throw new RuntimeException("未认证");
        }
        return u;
    }

    /**
     * 解除某课程与教材的绑定
     * @param textbookId 教材 ID
     * @param courseId   课程 ID
     */
    @DeleteMapping("/{textbookId}/courses/{courseId}")
    public Result<Void> unassignTextbook(
            @PathVariable Integer textbookId,
            @PathVariable Integer courseId) {
        ensureAuthenticated();
        try {
            textbookService.unassignTextbook(textbookId, courseId);
            return Result.success();
        } catch (Exception e) {
            return Result.error("解绑失败：" + e.getMessage());
        }
    }



    /**
     * 获取当前登录用户ID
     *
     * @return user_id
     */
    private Integer getAuthUserId() {
        @SuppressWarnings("unchecked")
        Map<String, Object> claims = (Map<String, Object>) ThreadLocalUtil.get();
        return (Integer) claims.get("user_id");
    }

    /**
     * 确保已登录，否则抛异常
     */
    private void ensureAuthenticated() {
        getAuthUsername();
    }
}
