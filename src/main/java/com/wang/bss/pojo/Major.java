package com.wang.bss.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "major", schema = "book_reservation_system")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Integer majorId;

    @Column(name = "major_name", nullable = false, length = 255)
    private String majorName;

    @Column(name = "major_description", columnDefinition = "TEXT")
    private String majorDescription;

    // 新增学院外键字段
    @Column(name = "college_id", insertable = false, updatable = false)
    private Integer collegeId;

    /** 与 College 的多对一关系 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    /** 与 Student 的一对多关系 */
    @OneToMany(mappedBy = "major", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();

    /** 与 Teacher 的一对多关系 */
    @OneToMany(mappedBy = "major", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Teacher> teachers = new HashSet<>();
}
