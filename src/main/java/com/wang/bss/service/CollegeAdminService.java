package com.wang.bss.service;

import com.wang.bss.pojo.CollegeAdmin;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * 学院管理员业务接口
 */
public interface CollegeAdminService {

    CollegeAdmin findByUsername(String username);

    CollegeAdmin findById(Integer userId);

    void update(CollegeAdmin admin);

    void updatePwd(Integer userId, String newPwd);

    void register(CollegeAdmin admin);

    List<CollegeAdmin> findAll();

    void deleteById(Integer userId);
}
