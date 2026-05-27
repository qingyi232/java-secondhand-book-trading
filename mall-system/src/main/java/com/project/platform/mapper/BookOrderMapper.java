package com.project.platform.mapper;

import com.project.platform.entity.BookOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BookOrderMapper {
    List<BookOrder> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM book_order WHERE id = #{id}")
    BookOrder selectById(Integer id);

    int insert(BookOrder entity);

    int updateById(BookOrder entity);

    @Select("SELECT * FROM book_order WHERE status = 3 AND create_time >= DATE_SUB(NOW(), INTERVAL #{day} DAY)")
    List<BookOrder> completedOrdersInDays(int day);

    @Select("SELECT * FROM book_order WHERE shop_id = #{shopId} AND status = 3 AND create_time >= DATE_SUB(NOW(), INTERVAL #{day} DAY)")
    List<BookOrder> completedOrdersByShopInDays(@Param("shopId") Integer shopId, @Param("day") int day);
}
