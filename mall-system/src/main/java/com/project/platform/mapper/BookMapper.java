package com.project.platform.mapper;

import com.project.platform.entity.Book;
import com.project.platform.vo.ValueNameVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BookMapper {
    List<Book> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM book WHERE id = #{id}")
    Book selectById(Integer id);

    @Select("SELECT * FROM book WHERE review_status = '已通过' AND status = '上架'")
    List<Book> list();

    int insert(Book entity);

    int updateById(Book entity);

    boolean removeByIds(List<Integer> ids);

    @Select("SELECT * FROM book WHERE review_status = '已通过' AND status = '上架' ORDER BY sales_volume DESC LIMIT #{size}")
    List<Book> salesVolumeTop(int size);

    @Select("SELECT bc.name AS name, COUNT(b.id) AS value FROM book_category bc LEFT JOIN book b ON b.category_id = bc.id AND b.review_status = '已通过' GROUP BY bc.id, bc.name")
    List<ValueNameVO> selectCategoryCount();

    @Select("SELECT bc.name AS name, COUNT(b.id) AS value FROM book_category bc LEFT JOIN book b ON b.category_id = bc.id AND b.shop_id = #{shopId} GROUP BY bc.id, bc.name")
    List<ValueNameVO> selectCategoryCountByShopId(Integer shopId);
}
