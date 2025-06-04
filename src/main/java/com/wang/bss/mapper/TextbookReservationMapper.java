package com.wang.bss.mapper;

import com.wang.bss.pojo.TextbookReservation;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>TextbookReservation 的 Mapper 接口，负责与数据库进行增删改查操作。</p>
 * <p>使用 MyBatis 注解或 XML 方式，以下示例以注解形式给出。</p>
 */
@Mapper
public interface TextbookReservationMapper {

    /**
     * <p>插入一条新的征订记录。</p>
     * <p>使用数据库默认生成的主键策略（自增 ID）。</p>
     *
     * @param tbr 要插入的实体
     * @return 影响行数
     */
    @Insert("INSERT INTO textbook_reservation(" +
            "textbook_id, user_id, reservation_quantity, order_date, textbook_name, textbook_price" +
            ") VALUES(" +
            "#{textbookId}, #{userId}, #{reservationQuantity}, #{orderDate}, #{textbookName}, #{textbookPrice}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "reservationId")
    void insert(TextbookReservation tbr);


    /**
     * <p>根据 user_id 和 textbook_id 统计符合条件的记录数。</p>
     * <p>用于判断用户是否已对该教材下单。</p>
     *
     * @param userId     用户 ID
     * @param textbookId 教材 ID
     * @return 计数结果
     */
    @Select("SELECT COUNT(*) FROM book_reservation_system.textbook_reservation " +
            "WHERE user_id = #{userId} AND textbook_id = #{textbookId}")
    Integer countByUserIdAndTextbookId(@Param("userId") Integer userId,
                                       @Param("textbookId") Integer textbookId);

    /**
     * <p>根据 user_id 查询该用户的所有征订记录。</p>
     *
     * @param userId 用户 ID
     * @return 符合条件的列表
     */
    @Select("SELECT reservation_id AS reservationId, textbook_id AS textbookId, " +
            "user_id AS userId, reservation_quantity AS reservationQuantity, " +
            "order_date AS orderDate " +
            "FROM book_reservation_system.textbook_reservation " +
            "WHERE user_id = #{userId}")
    List<TextbookReservation> selectByUserId(@Param("userId") Integer userId);

    /**
     * <p>根据 reservation_id 查询单条记录。</p>
     *
     * @param id 征订记录 ID
     * @return 对应实体或 null
     */
    @Select("SELECT reservation_id AS reservationId, textbook_id AS textbookId, " +
            "user_id AS userId, reservation_quantity AS reservationQuantity, " +
            "order_date AS orderDate " +
            "FROM book_reservation_system.textbook_reservation " +
            "WHERE reservation_id = #{id}")
    TextbookReservation selectById(@Param("id") Integer id);

    /**
     * <p>根据实体中非 null 字段生成动态 SQL 进行更新，仅更新被赋值的字段。</p>
     * <p>例如若只设置了 reservationQuantity，则只更新该列。</p>
     *
     * @param tbr 更新时包含 reservationId 和要修改的字段
     * @return 影响行数
     */
    @Update({
            "<script>",
            "UPDATE textbook_reservation",
            "<set>",
            "<if test=\"textbookId != null\">textbook_id = #{textbookId},</if>",
            "<if test=\"userId != null\">user_id = #{userId},</if>",
            "<if test=\"reservationQuantity != null\">reservation_quantity = #{reservationQuantity},</if>",
            "<if test=\"orderDate != null\">order_date = #{orderDate},</if>",
            "<if test=\"textbookName != null\">textbook_name = #{textbookName},</if>",
            "<if test=\"textbookPrice != null\">textbook_price = #{textbookPrice},</if>",
            "</set>",
            "WHERE reservation_id = #{reservationId}",
            "</script>"
    })
    void updateSelective(TextbookReservation tbr);


    /**
     * <p>删除指定 ID 的记录。</p>
     *
     * @param id 征订记录 ID
     * @return 影响行数
     */
    @Delete("DELETE FROM book_reservation_system.textbook_reservation WHERE reservation_id = #{id}")
    int deleteById(@Param("id") Integer id);

    /**
     * <p>查询所有征订记录（包含学生姓名、学院、专业）。</p>
     *
     * @return 全部 TextbookReservation 列表
     */
    @Select(
            "SELECT " +
                    "  r.reservation_id       AS reservationId, " +
                    "  r.textbook_id          AS textbookId, " +
                    "  r.user_id              AS userId, " +
                    "  r.reservation_quantity AS reservationQuantity, " +
                    "  r.order_date           AS orderDate, " +
                    "  r.textbook_name        AS textbookName, " +
                    "  r.textbook_price       AS textbookPrice, " +
                    "  s.student_name         AS studentName, " +
                    "  c.college_name         AS collegeName, " +
                    "  m.major_name           AS majorName " +
                    "FROM book_reservation_system.textbook_reservation r " +
                    "  LEFT JOIN book_reservation_system.student s ON r.user_id = s.user_id " +
                    "  LEFT JOIN book_reservation_system.major m   ON s.major_id = m.major_id " +
                    "  LEFT JOIN book_reservation_system.college c ON m.college_id = c.college_id"
    )
    List<TextbookReservation> selectAll();
}
