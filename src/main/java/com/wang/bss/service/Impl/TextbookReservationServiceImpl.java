package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TextbookReservationMapper;
import com.wang.bss.pojo.TextbookReservation;
import com.wang.bss.service.TextbookReservationService;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TextbookReservationServiceImpl implements TextbookReservationService {

    @Autowired
    private TextbookReservationMapper textbookReservationMapper;
    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Override
    public void add(TextbookReservation tbr, String username) {
        tbr.setOrderDate(LocalDateTime.now());

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer reservationId = (Integer) map.get("id");
        tbr.setReservationId(reservationId);


        textbookReservationMapper.add(tbr);


    }

    @Override
    public TextbookReservation getAllReservations(Integer userId) {
        return textbookReservationMapper.getAllReservations(userId);
    }

    @Override
    public TextbookReservation getReservationById(Integer id) {
        return textbookReservationMapper.getReservationById(id);
    }

    @Override
    public void update(TextbookReservation tbr) {
        tbr.setOrderDate(LocalDateTime.now());
        textbookReservationMapper.updateTextbook(tbr);
    }

    @Override
    public void delete(Integer reservationId) {
        textbookReservationMapper.delete(reservationId);
    }
}
