package com.project.platform.mapper;

import com.project.platform.entity.BookCollect;
import com.project.platform.vo.ValueNameVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface BookCollectMapper {
    List<BookCollect> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Select("SELECT * FROM book_collect WHERE book_id = #{bookId} AND user_id = #{userId}")
    BookCollect selectByBookIdAndUserId(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

    @Insert("INSERT INTO book_collect (book_id, user_id) VALUES (#{bookId}, #{userId})")
    int insert(BookCollect entity);

    @Delete("DELETE FROM book_collect WHERE id = #{id}")
    int deleteById(Integer id);

    @Select("SELECT bc.category_id AS name, COUNT(*) AS value FROM book_collect c " +
            "JOIN book bc ON c.book_id = bc.id WHERE c.user_id = #{userId} GROUP BY bc.category_id")
    List<ValueNameVO> statisticsCategoryIdByUserId(Integer userId);
}
