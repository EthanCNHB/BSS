package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TextbookMapper;
import com.wang.bss.pojo.Textbook;
import com.wang.bss.pojo.Result;
import com.wang.bss.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class TextbookServiceImpl implements TextbookService {

    private static final Logger logger = LoggerFactory.getLogger(TextbookServiceImpl.class);

    @Autowired
    private TextbookMapper textbookMapper;

    // 获取符合条件的教材列表
    @Override
    public List<Textbook> getTextbooksByConditions(Map<String, Object> conditions) {
        return textbookMapper.findByConditions(conditions);
    }

    // 获取所有教材
    @Override
    public List<Textbook> getAllTextbooks() {
        return textbookMapper.findAll(); // 获取所有教材
    }

    // 根据ID获取教材
    @Override
    public Textbook getTextbookById(Integer textbookId) {
        return textbookMapper.findById(textbookId);
    }

    // 添加教材
    @Transactional
    @Override
    public Result add(Textbook textbook) {
        try {
            textbookMapper.add(textbook);
            logger.info("教材 {} 已成功添加", textbook.getName());
            return Result.success("教材添加成功");
        } catch (Exception e) {
            logger.error("添加教材失败", e);
            return Result.error("添加教材失败");
        }
    }

    // 更新教材
    @Transactional
    @Override
    public Result updateTextbook(Textbook textbook) {
        try {
            textbookMapper.updateTextbook(textbook);
            logger.info("教材 ID: {} 已成功更新", textbook.getTextbookId());
            return Result.success(textbook);
        } catch (Exception e) {
            logger.error("更新教材失败", e);
            return Result.error("更新教材失败");
        }
    }

    // 删除教材
    @Transactional
    @Override
    public Result deleteTextbook(Integer textbookId) {
        try {
            boolean deleted = textbookMapper.deleteTextbook(textbookId);
            if (deleted) {
                logger.info("教材 ID: {} 已成功删除", textbookId);
                return Result.success("教材删除成功");
            } else {
                return Result.error("教材删除失败");
            }
        } catch (Exception e) {
            logger.error("删除教材失败", e);
            return Result.error("删除教材失败");
        }
    }

    @Override
    public void assignTextbook(Integer textbookId, Integer courseId) {
        textbookMapper.insertTextbookCourse(textbookId, courseId);
    }

    @Override
    public void unassignTextbook(Integer textbookId, Integer courseId) {
        textbookMapper.deleteTextbookCourse(textbookId, courseId);
    }

    /**
     * 根据 textbookId 将库存减少 delta 个。
     * @param textbookId 要扣减库存的教材 ID
     * @param delta      扣减数量（必须 > 0）
     * @throws RuntimeException 如果库存不足或教材不存在
     */
    @Transactional
    @Override
    public void decreaseStock(Integer textbookId, Integer delta) {
        if (delta == null || delta <= 0) {
            throw new RuntimeException("扣减库存的数量必须大于 0");
        }
        Integer currentStock = textbookMapper.selectStockById(textbookId);
        if (currentStock == null) {
            throw new RuntimeException("教材不存在，无法扣减库存");
        }
        if (currentStock < delta) {
            throw new RuntimeException("库存不足，无法扣减");
        }
        int updated = currentStock - delta;
        textbookMapper.updateStock(textbookId, updated);
        logger.info("教材 ID: {} 库存已扣减 {}，剩余库存 {}", textbookId, delta, updated);
    }

    /**
     * 根据 textbookId 将库存增加 delta 个（用于退回库存）。
     * @param textbookId 要增加库存的教材 ID
     * @param delta      增加数量（必须 > 0）
     */
    @Transactional
    @Override
    public void increaseStock(Integer textbookId, Integer delta) {
        if (delta == null || delta <= 0) {
            throw new RuntimeException("增加库存的数量必须大于 0");
        }
        Integer currentStock = textbookMapper.selectStockById(textbookId);
        if (currentStock == null) {
            throw new RuntimeException("教材不存在，无法增加库存");
        }
        int updated = currentStock + delta;
        textbookMapper.updateStock(textbookId, updated);
        logger.info("教材 ID: {} 库存已增加 {}，当前库存 {}", textbookId, delta, updated);
    }
}
