package com.wang.bss.pojo;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 学院管理员实体
 */
@Data
@Entity
@Table(name = "college_admin", schema = "book_reservation_system")
public class CollegeAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "college_id", insertable = false, updatable = false)
    private Integer collegeId;

    /** 与 College 的多对一关联 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;
}
