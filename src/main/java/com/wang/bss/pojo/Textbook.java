package com.wang.bss.pojo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "textbook", schema = "book_reservation_system")
public class Textbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "textbook_id")
    private Integer textbookId;  // 教材ID

    @NotEmpty(message = "教材名称不能为空")
    @Column(name = "name", nullable = false)
    private String name;         // 教材名称

    @NotEmpty(message = "教材编号不能为空")
    @Column(name = "code", nullable = false, unique = true)
    private String code;         // 教材编号

    @Column(name = "publisher")
    private String publisher;    // 出版社

    @Column(name = "author")
    private String author;       // 作者

    @Column(name = "price")
    private Double price;


    @Column(name = "status")
    private String status;       // 教材状态

    @Column(name = "stock_quantity")
    private Integer stockQuantity; // 库存数量

    /** 与 Course 的多对多 */
    @ManyToMany
    @JoinTable(
            name = "course_textbook",
            joinColumns = @JoinColumn(name = "textbook_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
