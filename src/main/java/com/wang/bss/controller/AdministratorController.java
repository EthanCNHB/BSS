package com.wang.bss.controller;

import com.wang.bss.pojo.Admin;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.AdministratorService;
import com.wang.bss.utils.JwtUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,1000}$") String password) {
        Admin loginAdmin = administratorService.findByUserName(username);
        if (loginAdmin == null) {
            return Result.error("用户名错误！");
        }

        if (password.equals(loginAdmin.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", loginAdmin.getUserId());
            claims.put("username", loginAdmin.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误！");
    }
}
