package com.wang.bss.service;

import com.wang.bss.pojo.Admin;
import jakarta.validation.constraints.Pattern;

public interface AdministratorService {

    //根据用户名查询管理员
    Admin findByUserName(String username);
}
