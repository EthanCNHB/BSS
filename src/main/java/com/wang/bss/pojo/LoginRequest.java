package com.wang.bss.pojo;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名必须为5到16个非空白字符")
    private String username;
    @Pattern(regexp = "^\\S{5,1000}$", message = "密码必须为5到1000个非空白字符")
    private String password;
}
