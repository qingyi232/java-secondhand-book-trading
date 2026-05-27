package com.project.platform.mapper;

import com.project.platform.entity.BookCategory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BookCategoryMapper {
    @Select("SELECT * FROM book_category ORDER BY id")
    List<BookCategory> list();

    @Select("SELECT * FROM book_category WHERE id = #{id}")
    BookCategory selectById(Integer id);

    @Insert("INSERT INTO book_category (name, remark) VALUES (#{name}, #{remark})")
    int insert(BookCategory entity);

    @Update("UPDATE book_category SET name = #{name}, remark = #{remark} WHERE id = #{id}")
    int updateById(BookCategory entity);

    @Delete("DELETE FROM book_category WHERE id = #{id}")
    int deleteById(Integer id);
}
