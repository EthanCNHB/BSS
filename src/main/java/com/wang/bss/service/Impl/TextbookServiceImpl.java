package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TextbookMapper;
import com.wang.bss.pojo.Textbook;
import com.wang.bss.service.TextbookService;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TextbookServiceImpl implements TextbookService {

    @Autowired
    private TextbookMapper textbookMapper;

    @Override
    public List<Textbook> getTextbooksByConditions(Map<String, Object> conditions) {
        return textbookMapper.findByConditions(conditions);
    }

    @Override
    public boolean add(Textbook textbook) {
        Map<String,Object> map = ThreadLocalUtil.get();
        // Integer textbookId = (Integer) map.get("textbookId");
        textbookMapper.add(textbook);
        return true;
    }

    @Override
    public boolean deleteTextbook(Integer textbookId) {
        textbookMapper.deleteTextbook(textbookId);
        return true;
    }

    @Override
    public Textbook updateTextbook(Textbook textbook) {
        textbookMapper.updateTextbook(textbook);
        return textbook;
    }

}
