package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.TextbookReservation;
import com.wang.bss.service.Impl.StudentServiceImpl;
import com.wang.bss.service.TextbookReservationService;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reservation")
public class TextbookReservationController {

    @Autowired
    private TextbookReservationService textbookReservationService;
    private StudentServiceImpl studentServiceImpl;

    // 查询所有征订记录
    @GetMapping("/list")
    public Result<TextbookReservation> getTextbookReservation() {

        // 从ThreadLocal中获取当前线程的用户信息，包括用户名
        Map<String, String> claims = ThreadLocalUtil.get();

        // 获取当前用户的用户名
        String username = claims.get("username");

        // 调用学生服务，根据用户名查询对应的UserId，并获取该用户的教材预定记录
        TextbookReservation reservation = textbookReservationService.getAllReservations(studentServiceImpl.findByUsername(username).getUserId());

        // 返回操作成功的结果，包含用户的预定记录
        return Result.success(reservation);

    }

    // 根据 ID 查询征订记录
    @GetMapping("/{id}") // 将该方法映射到GET请求到"/{id}"路径
    public Result<TextbookReservation> getReservationById(@PathVariable Integer id) {
        // 调用服务层根据ID获取征订记录
        TextbookReservation reservation = textbookReservationService.getReservationById(id);
        // 如果未找到记录，返回错误信息
        if (reservation == null) {
            return Result.error("未找到该征订记录");
        }
        // 返回成功结果，包含查询到的征订记录
        return Result.success(reservation);
    }

    // 新增征订记录
    @PostMapping("/add")
    public Result add(@RequestBody TextbookReservation tbr) {
        // 从ThreadLocal中获取当前线程的用户信息，包括用户名
        Map<String, String> claims = ThreadLocalUtil.get();

        // 获取当前用户的用户名
        String username = claims.get("username");

        // 使用用户名查询用户ID，并设置到TextbookReservation对象中
        // 这里假设findByUsername不会返回null，且该用户存在
        tbr.setUserId(studentServiceImpl.findByUsername(username).getUserId());

        // 调用服务层添加新的征订记录
        textbookReservationService.add(tbr, username);

        // 返回操作成功的结果
        return Result.success();
    }


    // 更新征订记录
    @PutMapping("/update") // 将该方法映射到PUT请求到"/update"路径
    public Result updateReservation(@RequestBody TextbookReservation tbr) {
        // 检查征订记录是否存在，如果不存在可返回错误
        TextbookReservation existingReservation = textbookReservationService.getReservationById(tbr.getReservationId());
        if (existingReservation == null) {
            return Result.error("未找到该征订记录，无法更新");
        }
        // 更新操作，调用服务层的更新方法
        textbookReservationService.update(tbr);
        // 返回操作成功的结果
        return Result.success();
    }

    // 删除征订记录
    @DeleteMapping("/{reservationId}") // 将该方法映射到DELETE请求到"/{id}"路径
    public Result deleteReservation(@PathVariable Integer reservationId) {
        // 检查征订记录是否存在
        TextbookReservation existingReservation = textbookReservationService.getReservationById(reservationId);
        if (existingReservation == null) {
            return Result.error("未找到该征订记录，无法删除");
        }

        // 调用服务层的删除方法
        textbookReservationService.delete(reservationId);

        // 返回操作成功的结果
        return Result.success();
    }

}
