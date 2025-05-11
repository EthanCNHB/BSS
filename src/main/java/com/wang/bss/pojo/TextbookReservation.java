package com.wang.bss.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data // 使用Lombok自动生成方法
public class TextbookReservation {

    private Integer reservationId; // 征订记录ID
    private Integer textbookId; // 教材ID，外键
    private Integer collegeId; // 学院ID
    private Integer majorId; // 专业ID
    private Integer userId; // 用户ID
    private Integer reservationQuantity; // 征订数量
    private LocalDateTime orderDate; // 订单时间

}
