package com.wang.bss.service;

import com.wang.bss.pojo.TextbookReservation;

import java.util.List;

/**
 * <p>教材征订服务接口，定义了对 TextbookReservation 实体的增删改查等业务方法。</p>
 */
public interface TextbookReservationService {

    /**
     * <p>新增一条征订记录，并根据 username 更新相应业务信息（如 orderDate）。</p>
     *
     * @param tbr      要插入的 TextbookReservation 实体
     * @param username 当前登录用户名，用于记录操作日志或设置 orderDate
     */
    void add(TextbookReservation tbr, String username);

    /**
     * <p>判断指定用户是否已经对某本教材下过订单。</p>
     * <p>底层应在数据库层对 (user_id, textbook_id) 建立联合唯一索引，并返回是否存在。</p>
     *
     * @param userId     用户 ID
     * @param textbookId 教材 ID
     * @return 如果已存在记录返回 true，否则返回 false
     */
    boolean existsByUserIdAndTextbookId(Integer userId, Integer textbookId);

    /**
     * <p>查询某个用户的所有征订记录。</p>
     *
     * @param userId 用户 ID
     * @return 该用户拥有的所有 TextbookReservation 列表
     */
    List<TextbookReservation> getAllReservations(Integer userId);

    /**
     * <p>根据 ID 查询单条征订记录。</p>
     *
     * @param id 征订记录 ID
     * @return 对应的 TextbookReservation 实体或 null
     */
    TextbookReservation getReservationById(Integer id);

    /**
     * <p>更新一条征订记录（仅修改传入实体中被设置的字段）。</p>
     *
     * @param tbr 要更新的 TextbookReservation 实体
     */
    void update(TextbookReservation tbr);

    /**
     * <p>删除指定 ID 的征订记录。</p>
     *
     * @param reservationId 征订记录 ID
     */
    void delete(Integer reservationId);

    /**
     * <p>查询所有学生的所有征订记录（仅供管理员或具有相应权限时使用）。</p>
     *
     * @return 全部 TextbookReservation 列表
     */
    List<TextbookReservation> findAll();
}
