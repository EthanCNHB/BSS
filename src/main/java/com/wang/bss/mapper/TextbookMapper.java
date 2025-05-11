package com.wang.bss.mapper;

import com.wang.bss.pojo.Textbook;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;
import java.util.Map;

@Mapper
public interface TextbookMapper {
    //添加教材
    @Insert("INSERT INTO textbook(name, code, publisher, author, price, status) VALUES(#{name}, #{code}, #{publisher}, #{author}, #{price}, #{status})")
    void add(Textbook textbook);

    //查询所有教材
    @SelectProvider(type = TextbookSqlProvider.class, method = "findByConditions")
    @Results({
            @Result(property = "textbookId", column = "textbook_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "code", column = "code"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "author", column = "author"),
            @Result(property = "price", column = "price"),
            @Result(property = "status", column = "status"),
    })
    List<Textbook> findByConditions(Map<String, Object> conditions);

    //删除教材
    @Delete("delete from textbook where textbook_id = #{textbookId}")
    boolean deleteTextbook(Integer textbookId);

    //更改教材
    @Update("UPDATE textbook SET name = #{name}, code = #{code}, publisher = #{publisher}, " +
            "author = #{author}, price = #{price}, status = #{status} " +
            "WHERE textbook_id = #{textbookId}")
    void updateTextbook(Textbook textbook);

}
