package com.wang.bss.service;

import com.wang.bss.pojo.Major;

import java.util.List;

public interface MajorService {

    List<Major> getAllMajors();

    Major getMajorById(Integer majorId);

    void addMajor(Major major);

    void updateMajor(Major major);

    void deleteMajor(Integer majorId);

    List<Major> getMajorsByCollegeId(Integer collegeId);
}
