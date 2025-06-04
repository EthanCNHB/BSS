package com.wang.bss.service;

import com.wang.bss.pojo.Textbook;
import com.wang.bss.pojo.Result;

import java.util.List;
import java.util.Map;

public interface TextbookService {

    // 获取符合条件的教材列表
    List<Textbook> getTextbooksByConditions(Map<String, Object> conditions);

    // 获取所有教材
    List<Textbook> getAllTextbooks();

    // 根据ID获取教材
    Textbook getTextbookById(Integer textbookId);

    // 添加教材
    Result add(Textbook textbook);

    // 更新教材
    Result updateTextbook(Textbook textbook);

    // 删除教材
    Result deleteTextbook(Integer textbookId);

    // 为课程分配教材
    void assignTextbook(Integer textbookId, Integer courseId);

    // 撤销某课程的教材分配
    void unassignTextbook(Integer textbookId, Integer courseId);

    /**
     * 根据 textbookId 将库存减少 delta 个。
     * @param textbookId 要扣减库存的教材 ID
     * @param delta      扣减数量（必须 > 0）
     * @throws RuntimeException 如果库存不足或教材不存在
     */
    void decreaseStock(Integer textbookId, Integer delta);

    /**
     * 根据 textbookId 将库存增加 delta 个（用于退回库存）。
     * @param textbookId 要增加库存的教材 ID
     * @param delta      增加数量（必须 > 0）
     */
    void increaseStock(Integer textbookId, Integer delta);
}
