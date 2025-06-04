package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Textbook;
import com.wang.bss.service.TeacherService;
import com.wang.bss.service.TextbookService;
import com.wang.bss.utils.ThreadLocalUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/textbook")
public class TextbookController {

    @Autowired
    private TextbookService textbookService;
    @Autowired
    private TeacherService teacherService;

    /**
     * 获取所有教材
     */
    @GetMapping("/list")
    public Result<List<Textbook>> getAllTextbooks() {
        ensureAuthenticated();
        return Result.success(textbookService.getAllTextbooks());
    }

    /**
     * 根据条件查询教材列表
     * 支持传入 name, publisher, minPrice, maxPrice 等任意组合
     */
    @GetMapping("/search")
    public Result<List<Textbook>> searchTextbooks(@RequestParam Map<String, Object> conditions) {
        ensureAuthenticated();
        List<Textbook> list = textbookService.getTextbooksByConditions(conditions);
        return Result.success(list);
    }

    /**
     * 根据ID获取教材
     */
    @GetMapping("/{textbookId}")
    public Result<Textbook> getTextbookById(@PathVariable Integer textbookId) {
        ensureAuthenticated();
        Textbook tb = textbookService.getTextbookById(textbookId);
        if (tb != null) {
            return Result.success(tb);
        }
        return Result.error("教材未找到");
    }

    /**
     * 添加教材
     */
    @PostMapping
    public Result<?> addTextbook(@Valid @RequestBody Textbook textbook) {
        ensureAuthenticated();
        if (!isValidPrice(textbook.getPrice())) {
            return Result.error("价格必须大于0");
        }
        return textbookService.add(textbook);
    }

    /**
     * 更新教材
     */
    @PutMapping
    public Result<?> updateTextbook(@Valid @RequestBody Textbook textbook) {
        ensureAuthenticated();
        if (!isValidPrice(textbook.getPrice())) {
            return Result.error("价格必须大于0");
        }
        return textbookService.updateTextbook(textbook);
    }

    /**
     * 删除教材
     */
    @DeleteMapping("/{textbookId}")
    public Result<?> deleteTextbook(@PathVariable Integer textbookId) {
        ensureAuthenticated();
        return textbookService.deleteTextbook(textbookId);
    }

    /**
     * 校验用户已认证，否则抛异常
     */
    private void ensureAuthenticated() {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) ThreadLocalUtil.get();
        if (user.get("username") == null) {
            throw new RuntimeException("未认证");
        }
    }

    /**
     * 给课程分配教材
     *
     * @param textbookId 教材ID
     * @param courseIds 课程ID列表
     * @return 无
     */
    @PostMapping("/{textbookId}/courses")
    public Result<Void> assignTextbook(
            @PathVariable Integer textbookId,
            @RequestBody List<Integer> courseIds) {
        ensureAuthenticated();

        // 如果 courseIds 为空，则返回错误提示
        if (courseIds == null || courseIds.isEmpty()) {
            return Result.error("请选择要分配的课程");
        }

        // 逐个插入课程
        for (Integer courseId : courseIds) {
            // 调用服务层方法逐个插入
            textbookService.assignTextbook(textbookId, courseId);
        }

        return Result.success();
    }

    /**
     * 撤销某位教师的一门课程
     *
     * @param teacherId 教师ID
     * @param courseId  课程ID
     * @return 无
     */
    @DeleteMapping("/{teacherId}/courses/{courseId}")
    public Result<Void> unassignCourse(
            @PathVariable Integer teacherId,
            @PathVariable Integer courseId) {
        ensureAuthenticated();
        teacherService.unassignCourse(teacherId, courseId);
        return Result.success();
    }

    /**
     * 校验教材价格（Double）大于 0
     */
    private boolean isValidPrice(Double price) {
        return price != null && price > 0;
    }
}
