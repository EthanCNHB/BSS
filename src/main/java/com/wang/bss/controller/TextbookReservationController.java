package com.wang.bss.controller;

import com.wang.bss.pojo.Result;
import com.wang.bss.pojo.Textbook;
import com.wang.bss.pojo.TextbookReservation;
import com.wang.bss.service.StudentService;
import com.wang.bss.service.TextbookReservationService;
import com.wang.bss.service.TextbookService;
import com.wang.bss.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>教材征订控制器，负责处理学生对教材征订的增删改查等操作。</p>
 * <p>所有修改、删除操作均做“归属校验”，确保只能操作当前登录用户自己的记录。</p>
 */
@RestController
@RequestMapping("/reservation")
public class TextbookReservationController {

    @Autowired
    private TextbookReservationService textbookReservationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TextbookService textbookService;

    /**
     * <p>查询所有学生的所有订单（仅供管理员或具有相应权限的角色使用）。</p>
     *
     * @return 包含所有教材征订记录的列表
     */
    @GetMapping("/allList")
    public Result<List<TextbookReservation>> getAllTextbookReservation() {
        List<TextbookReservation> reservations = textbookReservationService.findAll();
        return Result.success(reservations);
    }

    /**
     * <p>查询当前登录学生的所有征订记录。</p>
     * <p>自动补全返回结果中的教材名称和价格。</p>
     *
     * @return 当前学生所有的征订记录列表
     */
    @GetMapping("/list")
    public Result<List<TextbookReservation>> getTextbookReservation() {
        Integer userId = ensureAuthenticatedAndGetUserId();

        List<TextbookReservation> reservations =
                textbookReservationService.getAllReservations(userId);

        // 批量补全教材名称与价格
        reservations.forEach(this::enrichReservation);
        return Result.success(reservations);
    }

    /**
     * <p>根据 ID 查询单条征订记录。</p>
     * <p>仅允许查询当前用户自己的记录，否则返回错误提示。</p>
     *
     * @param id 征订记录 ID
     * @return 对应的征订记录实体
     */
    @GetMapping("/{id}")
    public Result<TextbookReservation> getReservationById(@PathVariable Integer id) {
        TextbookReservation r = textbookReservationService.getReservationById(id);
        if (r == null) {
            return Result.error("未找到该征订记录");
        }

        // 校验：只能查看自己名下的订单
        Integer currentUserId = ensureAuthenticatedAndGetUserId();
        if (!r.getUserId().equals(currentUserId)) {
            return Result.error("无权限查看该征订记录");
        }

        enrichReservation(r);
        return Result.success(r);
    }

    /**
     * <p>新增征订记录。</p>
     * <ol>
     *     <li>校验当前用户已经登录；</li>
     *     <li>检查用户是否已对同一本教材下过单（防止重复下单）；</li>
     *     <li>校验新传入的 reservationQuantity 合法性；</li>
     *     <li>将 userId、textbookName、textbookPrice 写入实体并调用 Service 层执行新增；</li>
     * </ol>
     *
     * @param tbr 前端传入的征订对象（需包含 textbookId 和 reservationQuantity 字段）
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result<Void> add(@RequestBody TextbookReservation tbr) {
        Integer userId = ensureAuthenticatedAndGetUserId();
        Integer textbookId = tbr.getTextbookId();
        Integer quantity = tbr.getReservationQuantity();

        // —— 参数完整性校验 —— //
        if (textbookId == null) {
            return Result.error("textbookId 不能为空");
        }
        if (quantity == null || quantity <= 0) {
            return Result.error("征订数量必须大于 0");
        }

        // —— 检查用户是否已对该教材下单 —— //
        boolean alreadyReserved = textbookReservationService
                .existsByUserIdAndTextbookId(userId, textbookId);
        if (alreadyReserved) {
            return Result.error("您已对该教材征订过，无需重复下单");
        }

        // —— 查询教材信息，填充教材名称和价格 —— //
        Textbook tb = textbookService.getTextbookById(textbookId);
        if (tb == null) {
            return Result.error("未找到该教材");
        }
        tbr.setTextbookName(tb.getName());
        tbr.setTextbookPrice(BigDecimal.valueOf(tb.getPrice()));

        // —— 写入 userId 并执行新增操作 —— //
        tbr.setUserId(userId);
        textbookReservationService.add(tbr, getAuthenticatedUsername());
        return Result.success();
    }


    /**
     * <p>修改征订数量。</p>
     * <ol>
     *     <li>仅允许修改当前登录用户自己的记录；</li>
     *     <li>只更新 reservationQuantity 字段，其它字段保持不变；</li>
     *     <li>校验新的数量合法性（必须大于 0）。</li>
     * </ol>
     *
     * @param payload 包含 reservationId 和 reservationQuantity 的 Map
     * @return 操作结果
     */
    @PutMapping("/updateQuantity")
    public Result<Void> updateQuantity(@RequestBody Map<String, Integer> payload) {
        Integer reservationId = payload.get("reservationId");
        Integer newQuantity = payload.get("reservationQuantity");

        if (reservationId == null || newQuantity == null) {
            return Result.error("缺少 reservationId 或 reservationQuantity 参数");
        }
        if (newQuantity <= 0) {
            return Result.error("征订数量必须大于 0");
        }

        TextbookReservation existing = getOwnedReservationOrNull(reservationId);
        if (existing == null) {
            return Result.error("订单不存在或无权限修改");
        }

        existing.setReservationQuantity(newQuantity);
        textbookReservationService.update(existing);
        return Result.success();
    }

    /**
     * <p>更新征订记录的指定字段。</p>
     * <p>目前仅允许修改 reservationQuantity 字段，且只能针对当前登录用户自己的记录。</p>
     * <p>若将来需要开放其它字段修改，可在此处扩展对应属性赋值逻辑。</p>
     *
     * @param tbr 前端传入的征订实体，必须包含 reservationId 和要修改的字段
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<Void> updateReservation(@RequestBody TextbookReservation tbr) {
        Integer reservationId = tbr.getReservationId();
        if (reservationId == null) {
            return Result.error("缺少 reservationId");
        }

        TextbookReservation existing = getOwnedReservationOrNull(reservationId);
        if (existing == null) {
            return Result.error("订单不存在或无权限修改");
        }

        // —— 只复制允许更新的字段 —— //
        Integer newQuantity = tbr.getReservationQuantity();
        if (newQuantity != null) {
            if (newQuantity <= 0) {
                return Result.error("征订数量必须大于 0");
            }
            existing.setReservationQuantity(newQuantity);
        }
        // 如果以后允许修改其他字段，只需在此处补充：
        // if (tbr.getXYZ() != null) {
        //     existing.setXYZ(tbr.getXYZ());
        // }

        textbookReservationService.update(existing);
        return Result.success();
    }

    /**
     * <p>删除征订记录。</p>
     * <p>仅允许删除当前登录用户自己名下的订单。</p>
     *
     * @param reservationId 要删除的征订记录 ID
     * @return 操作结果
     */
    @DeleteMapping("/{reservationId}")
    public Result<Void> deleteReservation(@PathVariable Integer reservationId) {
        TextbookReservation existing = getOwnedReservationOrNull(reservationId);
        if (existing == null) {
            return Result.error("订单不存在或无权限删除");
        }
        textbookReservationService.delete(reservationId);
        return Result.success();
    }

    // —— 私有辅助方法 —— //

    /**
     * <p>校验并获取当前登录用户自己的某条征订记录，若不存在或不属于当前用户则返回 null。</p>
     *
     * @param reservationId 征订记录 ID
     * @return 属于当前用户的征订记录实体，或 null
     */
    private TextbookReservation getOwnedReservationOrNull(Integer reservationId) {
        TextbookReservation r = textbookReservationService.getReservationById(reservationId);
        if (r == null) {
            return null;
        }
        Integer currentUserId = ensureAuthenticatedAndGetUserId();
        if (!r.getUserId().equals(currentUserId)) {
            return null;
        }
        return r;
    }

    /**
     * <p>从 ThreadLocal 中获取当前登录用户名对应的 studentId，若未登录或查询异常则抛出运行时异常。</p>
     *
     * @return 当前登录用户的 userId
     */
    private Integer ensureAuthenticatedAndGetUserId() {
        String username = getAuthenticatedUsername();
        Integer userId = studentService.findByUsername(username).getUserId();
        if (userId == null) {
            throw new RuntimeException("用户信息异常，userId 为空");
        }
        return userId;
    }

    /**
     * <p>从 ThreadLocal 中获取当前登录用户名，若未登录则抛出运行时异常。</p>
     *
     * @return 当前登录用户名
     */
    private String getAuthenticatedUsername() {
        @SuppressWarnings("unchecked")
        Map<String, Object> claims = (Map<String, Object>) ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        if (username == null || username.isBlank()) {
            throw new RuntimeException("未认证或会话已失效");
        }
        return username;
    }

    /**
     * <p>若征订记录实体中缺少教材名称或价格，则从教材服务中补全。</p>
     * <p>尽量减少对数据库的次数，可根据业务需求在 Service 层做批量查询优化。</p>
     *
     * @param r 待补全的征订记录实体
     */
    private void enrichReservation(TextbookReservation r) {
        if (r.getTextbookName() == null || r.getTextbookPrice() == null) {
            Textbook tb = textbookService.getTextbookById(r.getTextbookId());
            if (tb != null) {
                r.setTextbookName(tb.getName());
                // textbook.getPrice() 返回 Double，需要转换为 BigDecimal
                r.setTextbookPrice(BigDecimal.valueOf(tb.getPrice()));
            }
        }
    }
}
