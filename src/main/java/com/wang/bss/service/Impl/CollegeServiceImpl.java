package com.wang.bss.service.Impl;

import com.wang.bss.pojo.College;
import com.wang.bss.mapper.CollegeMapper;
import com.wang.bss.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeServiceImpl implements CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;

    @Override
    public List<College> getAllColleges() {
        return collegeMapper.getAllColleges();
    }

    @Override
    public College getCollegeById(Integer collegeId) {
        return collegeMapper.getCollegeById(collegeId);
    }

    @Override
    public void addCollege(College college) {
        collegeMapper.addCollege(college);
    }

    @Override
    public void updateCollege(College college) {
        collegeMapper.updateCollege(college);
    }

    @Override
    public void deleteCollege(Integer collegeId) {
        collegeMapper.deleteCollege(collegeId);
    }
}
