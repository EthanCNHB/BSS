package com.wang.bss.service;

import com.wang.bss.pojo.Student;

public interface StudentService {


    Student findByUsername(String username);

    void register(String username, String password);

    void update(Student student);

    void updatePwd(String newPwd);
}
