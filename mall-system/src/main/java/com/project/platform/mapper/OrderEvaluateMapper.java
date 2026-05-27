package com.project.platform.mapper;

import com.project.platform.entity.OrderEvaluate;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface OrderEvaluateMapper {
    List<OrderEvaluate> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM order_evaluate WHERE id = #{id}")
    OrderEvaluate selectById(Integer id);

    @Insert("INSERT INTO order_evaluate (user_id, book_id, order_id, content, rate) VALUES (#{userId}, #{bookId}, #{orderId}, #{content}, #{rate})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderEvaluate entity);

    @Select("SELECT oe.*, u.nickname AS userName FROM order_evaluate oe LEFT JOIN user u ON oe.user_id = u.id WHERE oe.book_id = #{bookId} ORDER BY oe.id DESC")
    List<OrderEvaluate> selectByBookId(Integer bookId);
}
