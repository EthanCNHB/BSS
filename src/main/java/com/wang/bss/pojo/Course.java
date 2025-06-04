package com.wang.bss.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "course", schema = "book_reservation_system")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_code", nullable = false, length = 20, unique = true)
    private String courseCode;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "course_type", length = 20)
    private String courseType;

    @Column(precision = 3, scale = 1)
    private BigDecimal credit;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "major_id")
    private Integer majorId;

    /** 与 Student 的多对多关系 */
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    /** 与 Teacher 的多对多关系 */
    @ManyToMany
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    /** 与 Textbook 的多对多，改用 List 保证和 Mapper 一致 */
    @ManyToMany
    @JoinTable(
            name = "course_textbook",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "textbook_id")
    )
    private List<Textbook> textbooks = new ArrayList<>();
}
