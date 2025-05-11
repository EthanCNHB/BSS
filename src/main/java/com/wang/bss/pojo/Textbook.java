package com.wang.bss.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // 导入Lombok的@Data注解，用于自动生成getter、setter等方法

@Data // 使用Lombok来自动生成各种方法
@Entity // 标识该类为一个实体类
@Table(name = "textbook", schema = "book_reservation_system") // 指定表名和架构
public class Textbook {
    @Id // 标识该字段为主键
    @NotNull // 该字段不能为空
    @Column(name = "textbook_id") // 映射数据库字段
    private Integer textbookId; // 教材ID

    @NotEmpty // 该字段不能为空
    @Column(name = "name") // 映射数据库字段
    private String name; // 教材名称

    @Column(name = "code") // 映射数据库字段
    private String code; // 教材编号

    @Column(name = "publisher") // 映射数据库字段
    private String publisher; // 出版社

    @Column(name = "author") // 映射数据库字段
    private String author; // 作者

    @Column(name = "price") // 映射数据库字段
    private Double price; // 价格

    @Column(name = "status") // 映射数据库字段
    private String status; // 教材状态
}
