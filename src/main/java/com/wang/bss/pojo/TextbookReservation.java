package com.wang.bss.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "textbook_reservation", schema = "book_reservation_system")
public class TextbookReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId; // 征订记录ID

    @Column(name = "textbook_id", nullable = false)
    private Integer textbookId; // 教材ID，外键

    @Column(name = "user_id")
    private Integer userId; // 用户ID

    @Column(name = "reservation_quantity")
    private Integer reservationQuantity; // 征订数量

    @Column(name = "order_date")
    private LocalDateTime orderDate; // 订单时间

    @Column(name = "textbook_name")
    private String textbookName;

    @Column(name = "textbook_price")
    private BigDecimal textbookPrice;

    @Transient
    private String studentName;

    @Transient
    private String collegeName;

    @Transient
    private String majorName;

    /**
     * 与 Textbook 的多对一关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "textbook_id", insertable = false, updatable = false)
    private Textbook textbook;

    /**
     * 与 Student 的多对一关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Student student;

}
