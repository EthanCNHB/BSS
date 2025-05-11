package com.wang.bss.service;

import com.wang.bss.pojo.Teacher;

public interface TeacherService {

    Teacher findByUserName(String username);

    void register(String username, String password);

    //更新
    void update(Teacher teacher);

    //更新头像
    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd);
}
