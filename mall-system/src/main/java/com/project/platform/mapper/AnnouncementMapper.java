package com.project.platform.mapper;

import com.project.platform.entity.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AnnouncementMapper {
    @Select("SELECT * FROM announcement ORDER BY create_time DESC")
    List<Announcement> list();

    @Select("SELECT * FROM announcement WHERE id = #{id}")
    Announcement selectById(Integer id);

    @Insert("INSERT INTO announcement (title, content) VALUES (#{title}, #{content})")
    int insert(Announcement entity);

    @Update("UPDATE announcement SET title = #{title}, content = #{content} WHERE id = #{id}")
    int updateById(Announcement entity);

    @Delete("DELETE FROM announcement WHERE id = #{id}")
    int deleteById(Integer id);
}
