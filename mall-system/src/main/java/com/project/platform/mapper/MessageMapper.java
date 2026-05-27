package com.project.platform.mapper;

import com.project.platform.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MessageMapper {
    @Select("SELECT m.*, b.book_name AS bookName FROM message m LEFT JOIN book b ON m.book_id = b.id " +
            "WHERE ((m.from_user_id = #{userId} AND m.from_user_type = #{userType}) " +
            "OR (m.to_user_id = #{userId} AND m.to_user_type = #{userType})) " +
            "ORDER BY m.create_time DESC")
    List<Message> listByUser(@Param("userId") Integer userId, @Param("userType") String userType);

    @Select("SELECT m.*, b.book_name AS bookName FROM message m LEFT JOIN book b ON m.book_id = b.id " +
            "WHERE ((m.from_user_id = #{userId1} AND m.from_user_type = #{userType1} AND m.to_user_id = #{userId2} AND m.to_user_type = #{userType2}) " +
            "OR (m.from_user_id = #{userId2} AND m.from_user_type = #{userType2} AND m.to_user_id = #{userId1} AND m.to_user_type = #{userType1})) " +
            "ORDER BY m.create_time ASC")
    List<Message> listConversation(@Param("userId1") Integer userId1, @Param("userType1") String userType1,
                                   @Param("userId2") Integer userId2, @Param("userType2") String userType2);

    @Insert("INSERT INTO message (from_user_id, from_user_type, to_user_id, to_user_type, content, book_id) " +
            "VALUES (#{fromUserId}, #{fromUserType}, #{toUserId}, #{toUserType}, #{content}, #{bookId})")
    int insert(Message message);

    @Update("UPDATE message SET is_read = 1 WHERE to_user_id = #{userId} AND to_user_type = #{userType} AND is_read = 0")
    int markAllRead(@Param("userId") Integer userId, @Param("userType") String userType);

    @Select("SELECT COUNT(*) FROM message WHERE to_user_id = #{userId} AND to_user_type = #{userType} AND is_read = 0")
    int unreadCount(@Param("userId") Integer userId, @Param("userType") String userType);
}
