package com.wang.bss.pojo;


import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data // 使用Lombok来自动生成方法
public class Teacher {
    private Integer userId;

    private String teacherName;

    private Integer collegeId;

    private Integer majorId;

    private String username;

    @JsonIgnore
    private String password;
}
