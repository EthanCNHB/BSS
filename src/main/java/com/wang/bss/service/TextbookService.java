package com.wang.bss.service;

import com.wang.bss.pojo.Textbook;

import java.util.List;
import java.util.Map;

public interface TextbookService {

    //教材查询
    List<Textbook> getTextbooksByConditions(Map<String, Object> params);

    //添加图书
    boolean add(Textbook textbook);

    //删除图书
    boolean deleteTextbook(Integer textbookId);

    //更改图书信息
    Textbook updateTextbook(Textbook textbook);

}