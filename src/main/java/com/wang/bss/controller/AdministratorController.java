package com.wang.bss.controller;

import com.wang.bss.pojo.Admin;
import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.TextbookReservation;
import com.wang.bss.service.AdministratorService;
import com.wang.bss.service.TextbookReservationService;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private TextbookReservationService textbookReservationService;
    // 获取管理员资料
    @GetMapping("/info")
    public Result<Admin> adminInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        Admin admin = administratorService.findByUserName(username);
        return Result.success(admin);
    }

    /**
     * 获取所有教材订单
     */
    @GetMapping("/trInfo")
    public Result<List<TextbookReservation>> findAll(){
        return Result.success(textbookReservationService.findAll());
    }

}