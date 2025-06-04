package com.wang.bss.service.Impl;

import com.wang.bss.mapper.CollegeAdminMapper;
import com.wang.bss.pojo.CollegeAdmin;
import com.wang.bss.service.CollegeAdminService;
import com.wang.bss.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeAdminServiceImpl implements CollegeAdminService {

    @Autowired
    private CollegeAdminMapper collegeAdminMapper;

    @Override
    public CollegeAdmin findByUsername(String username) {
        return collegeAdminMapper.findByUsername(username);
    }

    @Override
    public CollegeAdmin findById(Integer userId) {
        return collegeAdminMapper.findById(userId);
    }

    @Override
    public void update(CollegeAdmin admin) {
        collegeAdminMapper.update(admin);
    }

    @Override
    public void updatePwd(Integer userId, String newPwd) {
        String md5 = Md5Util.getMD5String(newPwd);
        collegeAdminMapper.updatePwd(userId, md5);
    }

    @Override
    public void register(CollegeAdmin admin) {
        collegeAdminMapper.add(admin);
    }

    @Override
    public List<CollegeAdmin> findAll() {
        return collegeAdminMapper.findAll();
    }

    /** 删除管理员 */
    @Override
    public void deleteById(Integer userId) {
        collegeAdminMapper.deleteById(userId);
    }
}
