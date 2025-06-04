package com.wang.bss.controller;

import com.wang.bss.pojo.Major;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping("/list")
    public Result<List<Major>> getAllMajors() {
        List<Major> majors = majorService.getAllMajors();
        return Result.success(majors);
    }

    @GetMapping("/{majorId}")
    public Result<Major> getMajorById(@PathVariable Integer majorId) {
        Major major = majorService.getMajorById(majorId);
        return major != null ? Result.success(major) : Result.error("专业未找到");
    }

    @PostMapping("/add")
    public Result<Void> addMajor(@RequestBody Major major) {
        majorService.addMajor(major);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateMajor(@RequestBody Major major) {
        majorService.updateMajor(major);
        return Result.success();
    }

    @DeleteMapping("/delete/{majorId}")
    public Result<Void> deleteMajor(@PathVariable Integer majorId) {
        majorService.deleteMajor(majorId);
        return Result.success();
    }

    // 根据collegeId获取专业列表
    @GetMapping("/listByCollege/{collegeId}")
    public Result<List<Major>> getMajorsByCollegeId(@PathVariable Integer collegeId) {
        List<Major> majors = majorService.getMajorsByCollegeId(collegeId);
        return Result.success(majors);
    }

}
