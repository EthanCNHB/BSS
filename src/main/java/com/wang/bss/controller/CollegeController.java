package com.wang.bss.controller;

import com.wang.bss.pojo.College;
import com.wang.bss.pojo.Major;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.CollegeService;
import com.wang.bss.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping("/list")
    public Result<List<College>> getAllColleges() {
        List<College> colleges = collegeService.getAllColleges();
        return Result.success(colleges);
    }

    @GetMapping("/{collegeId}")
    public Result<College> getCollegeById(@PathVariable Integer collegeId) {
        College college = collegeService.getCollegeById(collegeId);
        return college != null ? Result.success(college) : Result.error("学院未找到");
    }

    @PostMapping("/add")
    public Result<Void> addCollege(@RequestBody College college) {
        collegeService.addCollege(college);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<College> updateCollege(@RequestBody College college) {
         collegeService.updateCollege(college);
        return Result.success(college);
    }

    @DeleteMapping("/delete/{collegeId}")
    public Result<Void> deleteCollege(@PathVariable Integer collegeId) {
        collegeService.deleteCollege(collegeId);
        return Result.success();
    }


}
