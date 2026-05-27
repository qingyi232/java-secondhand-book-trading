package com.project.platform.service;

import com.project.platform.entity.Book;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface BookService {
    PageVO<Book> page(Map<String, Object> query, Integer pageNum, Integer pageSize);
    Book selectById(Integer id);
    List<Book> list();
    void insert(Book entity);
    void updateById(Book entity);
    void removeByIds(List<Integer> ids);
    void review(Integer id, String reviewStatus);
    List<Book> salesVolumeTop(int size);
    List<Book> recommend(Integer size);
    void updateStock(Integer id, Integer quantity, boolean isIncrease);
}
