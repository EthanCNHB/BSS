package com.wang.bss.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Student {
    private int userId;
    private String username;
    @JsonIgnore
    private String password;
    private int majorId;
    private int collegeId;
    private String studentName;
}
