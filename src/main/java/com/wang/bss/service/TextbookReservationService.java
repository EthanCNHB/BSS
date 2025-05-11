package com.wang.bss.service;

import com.wang.bss.pojo.TextbookReservation;

public interface TextbookReservationService {

    void add(TextbookReservation tbr,String username);

    TextbookReservation getAllReservations(Integer userId);

    TextbookReservation getReservationById(Integer id);

    void update(TextbookReservation tbr);

    void delete(Integer reservationId);
}
