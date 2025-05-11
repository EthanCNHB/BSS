package com.wang.bss.service.Impl;

import com.wang.bss.mapper.AdministratorMapper;
import com.wang.bss.pojo.Admin;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.AdministratorService;
import com.wang.bss.utils.JwtUtil;
import com.wang.bss.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdministratorImpl implements AdministratorService {

    @Autowired
    AdministratorMapper administratorMapper;

    @Override
    public Admin findByUserName(String username) {
        Admin admin = administratorMapper.findByUserName(username);
        return admin;
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        Admin loginAdmin = administratorMapper.findByUserName(username);
        if (loginAdmin == null) {
            return Result.error("管理员用户名错误！");
        }

        if (Md5Util.getMD5String(password).equals(loginAdmin.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", loginAdmin.getUserId());
            claims.put("username", loginAdmin.getUsername());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }
        return Result.error("密码错误！");
    }
}
