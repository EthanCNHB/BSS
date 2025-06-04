package com.wang.bss.service;

import com.wang.bss.pojo.College;

import java.util.List;

public interface CollegeService {

    List<College> getAllColleges();

    College getCollegeById(Integer collegeId);

    void addCollege(College college);

    void updateCollege(College college);

    void deleteCollege(Integer collegeId);
}
