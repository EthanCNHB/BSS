package com.wang.bss.mapper;

import com.wang.bss.pojo.TextbookReservation;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TextbookReservationMapper {

    @Insert("INSERT INTO textbook_reservation(textbook_id, college_id, major_id, user_id, reservation_quantity, order_date) " +
            "VALUES(#{textbookId}, #{collegeId}, #{majorId}, #{userId}, #{reservationQuantity}, #{orderDate})")
    void add(TextbookReservation tbr);

    @Select("select * from textbook_reservation where user_id=#{userId}")
    TextbookReservation getAllReservations(Integer userId);

    @Select("select * from textbook_reservation where reservation_id=#{id}")
    TextbookReservation getReservationById(Integer id);

    @Update("update textbook_reservation set textbook_id =#{textbookId},college_id=#{collegeId}" +
            "major_id = #{majorId},user_id = #{userId},reservation_quantity=#{reservationQuantity}," +
            "order_date=#{orderDate} where reservation_id=#{reservationId}")
    void updateTextbook(TextbookReservation tbr);

    @Delete("delete from textbook_reservation where reservation_id=#{reservationId}")
    void delete(Integer reservationId);
}
