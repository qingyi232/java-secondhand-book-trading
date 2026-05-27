package com.project.platform.mapper;

import com.project.platform.entity.ShoppingCart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ShoppingCartMapper {
    List<ShoppingCart> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM shopping_cart WHERE id = #{id}")
    ShoppingCart selectById(Integer id);

    @Select("SELECT * FROM shopping_cart")
    List<ShoppingCart> list();

    int insert(ShoppingCart entity);

    int updateById(ShoppingCart entity);

    boolean removeByIds(List<Integer> ids);

    @Select("SELECT * FROM shopping_cart WHERE book_id = #{bookId} AND user_id = #{userId}")
    ShoppingCart selectByBookIdAndUserId(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Select("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    boolean removeByUserId(Integer userId);
}
