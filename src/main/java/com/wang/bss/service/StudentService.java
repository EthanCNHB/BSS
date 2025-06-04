package com.wang.bss.service;

import com.wang.bss.pojo.Student;

import java.util.List;

public interface StudentService {


    Student findByUsername(String username);

    void register(String username, String password);

    void update(Student student);

    void updatePwd(String newPwd);


    Student findById(Integer userId);

    /**
     * 查询所有学生列表
     * @return 全部 Student 对象集合
     */
    List<Student> findAll();
}
