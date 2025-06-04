package com.wang.bss.service.Impl;

import com.wang.bss.mapper.StudentMapper;
import com.wang.bss.pojo.Student;
import com.wang.bss.service.StudentService;
import com.wang.bss.utils.Md5Util;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // 提供默认值
        String defaultName = "新注册学生";
        int defaultCollegeId = 1;
        int defaultMajorId = 1;
        String defaultAvatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";

        studentMapper.add(username, md5String, defaultName, defaultCollegeId, defaultMajorId, defaultAvatar);
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

    @Override
    public Student findById(Integer userId) {
        return studentMapper.selectById(userId);
    }

    @Override
    public List<Student> findAll() {
        return studentMapper.selectAll();
    }
}
