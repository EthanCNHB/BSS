package com.wang.bss.mapper;

import com.wang.bss.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdministratorMapper {

    @Select("select * from admin where username = #{username}")
    Admin findByUserName(String username);


}
