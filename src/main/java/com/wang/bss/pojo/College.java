package com.wang.bss.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "college", schema = "book_reservation_system")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    private Integer collegeId;

    @Column(name = "college_name", nullable = false, length = 255)
    private String collegeName;

    @Column(name = "creation_time")
    private String creationTime;

    @Column(name = "college_description", columnDefinition = "TEXT")
    private String collegeDescription;

    /** 与 Major 的一对多关系 */
    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Major> majors = new HashSet<>();

    /** 与 Teacher 的一对多关系 */
    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Teacher> teachers = new HashSet<>();

    /** 与 Student 的一对多关系 */
    @OneToMany(mappedBy = "college", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students = new HashSet<>();


}
