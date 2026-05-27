package com.project.platform.mapper;

import com.project.platform.entity.BrowsingHistory;
import com.project.platform.vo.ValueNameVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface BrowsingHistoryMapper {
    List<BrowsingHistory> queryPage(Integer offset, Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String, Object> query);

    @Insert("INSERT INTO browsing_history (book_id, user_id) VALUES (#{bookId}, #{userId})")
    int insert(BrowsingHistory entity);

    @Delete("DELETE FROM browsing_history WHERE id IN (${ids})")
    boolean removeByIds(@Param("ids") String ids);

    @Select("SELECT b.category_id AS name, COUNT(*) AS value FROM browsing_history bh " +
            "JOIN book b ON bh.book_id = b.id WHERE bh.user_id = #{userId} GROUP BY b.category_id")
    List<ValueNameVO> statisticsCategoryIdByUserId(Integer userId);
}
