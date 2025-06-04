package com.wang.bss.service.Impl;

import com.wang.bss.mapper.TextbookReservationMapper;
import com.wang.bss.pojo.TextbookReservation;
import com.wang.bss.service.TextbookReservationService;
import com.wang.bss.service.TextbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>TextbookReservationService 的实现类，负责与数据库 Mapper 层交互并完成业务逻辑。</p>
 * <p>所有方法均在事务控制下执行，确保数据一致性。</p>
 */
@Service
public class TextbookReservationServiceImpl implements TextbookReservationService {

    @Autowired
    private TextbookReservationMapper reservationMapper;

    @Autowired
    private TextbookService textbookService;

    /**
     * <p>新增征订记录。</p>
     * <ol>
     *   <li>设置 orderDate 为当前时间；</li>
     *   <li>插入 reservation：若事务回滚会自动取消库存扣减；</li>
     *   <li>调用 textbookService.decreaseStock 扣减库存。</li>
     * </ol>
     */
    @Override
    @Transactional
    public void add(TextbookReservation tbr, String username) {
        tbr.setOrderDate(LocalDateTime.now());
        reservationMapper.insert(tbr);
        textbookService.decreaseStock(tbr.getTextbookId(), tbr.getReservationQuantity());
    }


    /**
     * <p>判断指定用户是否已经对某本教材下过订单。</p>
     * <p>底层 Mapper 使用 SELECT COUNT(*) ... WHERE user_id = #{userId} AND textbook_id = #{textbookId}。</p>
     *
     * @param userId     用户 ID
     * @param textbookId 教材 ID
     * @return 如果已存在记录返回 true，否则返回 false
     */
    @Override
    public boolean existsByUserIdAndTextbookId(Integer userId, Integer textbookId) {
        Integer count = reservationMapper.countByUserIdAndTextbookId(userId, textbookId);
        return count != null && count > 0;
    }

    /**
     * <p>查询某个用户的所有征订记录。</p>
     * <p>Mapper 层根据 user_id 进行过滤，返回一个 List<TextbookReservation>。</p>
     *
     * @param userId 用户 ID
     * @return 该用户所有的征订记录
     */
    @Override
    public List<TextbookReservation> getAllReservations(Integer userId) {
        return reservationMapper.selectByUserId(userId);
    }

    /**
     * <p>根据主键 ID 查询单条征订记录。</p>
     *
     * @param id 征订记录 ID
     * @return 对应的实体或 null
     */
    @Override
    public TextbookReservation getReservationById(Integer id) {
        return reservationMapper.selectById(id);
    }

    /**
     * <p>更新一条征订记录（仅更新实体 tbr 中被设置过的字段），并同步库存变化。</p>
     * <ol>
     *   <li>先查询原始记录，得到 oldQty；</li>
     *   <li>计算 delta = newQty - oldQty；</li>
     *   <li>如果 delta > 0，则调用 decreaseStock 扣减；如果 delta < 0，则调用 increaseStock 补回；</li>
     *   <li>最后调用 updateSelective 更新数据库中的订购数量字段。</li>
     * </ol>
     *
     * @param tbr 要更新的实体，必须包含 reservationId 和 reservationQuantity
     */
    @Override
    @Transactional
    public void update(TextbookReservation tbr) {
        // 1. 查询原始记录
        TextbookReservation existing = reservationMapper.selectById(tbr.getReservationId());
        if (existing == null) {
            throw new RuntimeException("要更新的征订记录不存在");
        }

        Integer oldQty = existing.getReservationQuantity();
        Integer newQty = tbr.getReservationQuantity();
        if (newQty == null) {
            throw new RuntimeException("新的订购数量不能为空");
        }

        int delta = newQty - oldQty;
        if (delta > 0) {
            // 订购数量增加，库存需要再扣减 delta
            textbookService.decreaseStock(existing.getTextbookId(), delta);
        } else if (delta < 0) {
            // 订购数量减少，需要把 (-delta) 的库存退回
            textbookService.increaseStock(existing.getTextbookId(), -delta);
        }

        // 2. 更新数据库（仅更新非 null 字段，mapper 中定义了动态 SQL）
        reservationMapper.updateSelective(tbr);
    }

    /**
     * <p>删除指定 ID 的征订记录，并将对应库存退回。</p>
     * <ol>
     *   <li>查询出该记录的 reservationQuantity；</li>
     *   <li>调用 increaseStock 将该数量退回库存；</li>
     *   <li>最后删除这条记录。</li>
     * </ol>
     *
     * @param reservationId 征订记录 ID
     */
    @Override
    @Transactional
    public void delete(Integer reservationId) {
        // 1. 查询要删除的记录
        TextbookReservation existing = reservationMapper.selectById(reservationId);
        if (existing == null) {
            throw new RuntimeException("要删除的征订记录不存在");
        }
        // 2. 退回库存
        Integer qty = existing.getReservationQuantity();
        textbookService.increaseStock(existing.getTextbookId(), qty);
        // 3. 删除记录
        reservationMapper.deleteById(reservationId);
    }

    /**
     * <p>查询所有学生的所有征订记录。</p>
     * <p>此方法可供管理员或有权限的角色调用，不做 userId 过滤。</p>
     *
     * @return 全部 TextbookReservation 列表
     */
    @Override
    public List<TextbookReservation> findAll() {
        return reservationMapper.selectAll();
    }
}
