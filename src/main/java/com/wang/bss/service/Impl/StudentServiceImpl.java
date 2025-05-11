package com.wang.bss.service.Impl;

import com.wang.bss.mapper.StudentMapper;
import com.wang.bss.pojo.Student;
import com.wang.bss.service.StudentService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public Student findByUsername(String username) {
        Student std = studentMapper.findByUsername(username);
        return std;
    }

    @Override
    public void register(String username, String password) {

        String md5String = Md5Util.getMD5String(password);

        studentMapper.add(username, md5String);
    }

    @Override
    public void update(Student student) {

        studentMapper.update(student);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("userId");
        studentMapper.updatePwd(Md5Util.getMD5String(newPwd), userId);
    }
}
