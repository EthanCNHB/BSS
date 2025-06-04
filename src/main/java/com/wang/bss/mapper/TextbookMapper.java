package com.wang.bss.mapper;

import com.wang.bss.pojo.Textbook;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TextbookMapper {

    /**
     * 添加教材
     */
    @Insert("INSERT INTO textbook(name, code, publisher, author, price, status, stock_quantity) " +
            "VALUES (#{name}, #{code}, #{publisher}, #{author}, #{price}, #{status}, #{stockQuantity})")
    @Options(useGeneratedKeys = true, keyProperty = "textbookId")
    void add(Textbook textbook);

    /**
     * 查询所有教材
     */
    @Select("SELECT * FROM textbook")
    List<Textbook> findAll();

    /**
     * 根据教材ID查询教材
     */
    @Select("SELECT * FROM textbook WHERE textbook_id = #{textbookId}")
    Textbook findById(@Param("textbookId") Integer textbookId);

    /**
     * 根据条件查询教材
     * 支持按名称模糊、出版社、价格区间过滤
     * 可传入 keys: name, publisher, minPrice, maxPrice
     */
    @Select({
            "<script>",
            "SELECT * FROM textbook",
            "<where>",
            "<if test=\"name != null and name != ''\">",
            "AND name LIKE CONCAT('%', #{name}, '%')",
            "</if>",
            "<if test=\"publisher != null and publisher != ''\">",
            "AND publisher = #{publisher}",
            "</if>",
            "<if test=\"minPrice != null\">",
            "AND price &gt;= #{minPrice}",
            "</if>",
            "<if test=\"maxPrice != null\">",
            "AND price &lt;= #{maxPrice}",
            "</if>",
            "</where>",
            "</script>"
    })
    List<Textbook> findByConditions(Map<String, Object> conditions);

    /**
     * 删除教材
     * @return true if at least one row was deleted
     */
    @Delete("DELETE FROM textbook WHERE textbook_id = #{textbookId}")
    boolean deleteTextbook(@Param("textbookId") Integer textbookId);

    /**
     * 更新教材信息，包括库存
     */
    @Update("UPDATE textbook SET name = #{name}, code = #{code}, publisher = #{publisher}, " +
            "author = #{author}, price = #{price}, status = #{status}, stock_quantity = #{stockQuantity} " +
            "WHERE textbook_id = #{textbookId}")
    void updateTextbook(Textbook textbook);

    /**
     * 插入 课本-课程 关联
     */
    @Insert("INSERT INTO course_textbook (textbook_id, course_id) VALUES (#{textbookId}, #{courseId})")
    void insertTextbookCourse(@Param("textbookId") Integer textbookId, @Param("courseId") Integer courseId);

    /**
     * 通过 course_id 查询教材列表
     */
    @Select("""
      SELECT t.*
      FROM textbook t
      JOIN course_textbook ct ON t.textbook_id = ct.textbook_id
      WHERE ct.course_id = #{courseId}
      """)
    List<Textbook> findByCourseId(@Param("courseId") Integer courseId);

    /**
     * 删除 课本-课程 关联
     */
    @Delete("DELETE FROM course_textbook WHERE textbook_id = #{textbookId} AND course_id = #{courseId}")
    void deleteTextbookCourse(@Param("textbookId") Integer textbookId,
                              @Param("courseId") Integer courseId);

    /**
     * 查询当前教材库存
     */
    @Select("SELECT stock_quantity FROM textbook WHERE textbook_id = #{textbookId}")
    Integer selectStockById(@Param("textbookId") Integer textbookId);

    /**
     * 更新当前教材库存
     */
    @Update("UPDATE textbook SET stock_quantity = #{stock} WHERE textbook_id = #{textbookId}")
    void updateStock(@Param("textbookId") Integer textbookId, @Param("stock") Integer stock);
}
