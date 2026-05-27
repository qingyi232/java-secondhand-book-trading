package com.project.platform.controller;

import com.project.platform.entity.BookCategory;
import com.project.platform.mapper.BookCategoryMapper;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookCategory")
public class BookCategoryController {
    @Resource
    private BookCategoryMapper bookCategoryMapper;

    @GetMapping("list")
    public ResponseVO<List<BookCategory>> list() {
        return ResponseVO.ok(bookCategoryMapper.list());
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody BookCategory entity) {
        bookCategoryMapper.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO update(@RequestBody BookCategory entity) {
        bookCategoryMapper.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete/{id}")
    public ResponseVO delete(@PathVariable Integer id) {
        bookCategoryMapper.deleteById(id);
        return ResponseVO.ok();
    }
}
