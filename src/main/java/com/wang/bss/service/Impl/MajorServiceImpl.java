package com.wang.bss.service.Impl;

import com.wang.bss.pojo.Major;
import com.wang.bss.mapper.MajorMapper;
import com.wang.bss.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    private MajorMapper majorMapper;

    @Override
    public List<Major> getAllMajors() {
        return majorMapper.getAllMajors();
    }

    @Override
    public Major getMajorById(Integer majorId) {
        return majorMapper.getMajorById(majorId);
    }

    @Override
    public void addMajor(Major major) {
        majorMapper.addMajor(major);
    }

    @Override
    public void updateMajor(Major major) {
        majorMapper.updateMajor(major);
    }

    @Override
    public void deleteMajor(Integer majorId) {
        majorMapper.deleteMajor(majorId);
    }

    @Override
    public List<Major> getMajorsByCollegeId(Integer collegeId) {
        return majorMapper.findByCollegeId(collegeId);
    }
}
