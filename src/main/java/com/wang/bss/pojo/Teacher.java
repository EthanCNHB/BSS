package com.wang.bss.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "teacher", schema = "book_reservation_system")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "college_id", insertable = false, updatable = false)
    private Integer collegeId;

    @Column(name = "major_id", insertable = false, updatable = false)
    private Integer majorId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "teacher_pic")
    private String teacherPic;

    /** 与 Major 的多对一关系，对应 Major.teachers 中 mappedBy="major" */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    /** 与 College 的多对一关系，对应 College.teachers 中 mappedBy="teachers"（如有） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    /** 与 Course 的多对多关联 */
    @ManyToMany
    @JoinTable(
            name = "teacher_course",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
