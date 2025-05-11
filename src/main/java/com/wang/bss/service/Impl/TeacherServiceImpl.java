package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TeacherMapper;
import com.wang.bss.pojo.Teacher;
import com.wang.bss.service.TeacherService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public Teacher findByUserName(String username) {
        Teacher teacher = teacherMapper.findByUserName(username);
        return teacher;
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5String = Md5Util.getMD5String(password);
        teacherMapper.add(username, md5String);
    }

    @Override
    public void update(Teacher teacher) {
        //加密
        // teacher.setPassword(Md5Util.getMD5String(teacher.getPassword()));
        teacherMapper.update(teacher);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("userId");
        teacherMapper.updateAvatar(avatarUrl,userId);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("userId");
        teacherMapper.updatePwd(Md5Util.getMD5String(newPwd),userId);
    }
}
