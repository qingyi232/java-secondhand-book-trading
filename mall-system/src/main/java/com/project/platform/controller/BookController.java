package com.project.platform.controller;

import com.project.platform.entity.Book;
import com.project.platform.entity.BookCollect;
import com.project.platform.mapper.BookCollectMapper;
import com.project.platform.service.BookService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book")
public class BookController {
    @Resource
    private BookService bookService;
    @Resource
    private BookCollectMapper bookCollectMapper;

    @GetMapping("page")
    public ResponseVO<PageVO<Book>> page(@RequestParam Map<String, Object> query,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseVO.ok(bookService.page(query, pageNum, pageSize));
    }

    @GetMapping("selectById/{id}")
    public ResponseVO<Book> selectById(@PathVariable Integer id) {
        Book entity = bookService.selectById(id);
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            BookCollect collect = bookCollectMapper.selectByBookIdAndUserId(id, CurrentUserThreadLocal.getCurrentUser().getId());
            if (collect != null) {
                entity.setBookCollectId(collect.getId());
            }
        }
        return ResponseVO.ok(entity);
    }

    @GetMapping("list")
    public ResponseVO<List<Book>> list() {
        return ResponseVO.ok(bookService.list());
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody Book entity) {
        bookService.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO update(@RequestBody Book entity) {
        bookService.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        bookService.removeByIds(ids);
        return ResponseVO.ok();
    }

    @PutMapping("review/{id}")
    public ResponseVO review(@PathVariable Integer id, @RequestParam String reviewStatus) {
        bookService.review(id, reviewStatus);
        return ResponseVO.ok();
    }

    @GetMapping("salesVolumeTop/{size}")
    public ResponseVO<List<Book>> salesVolumeTop(@PathVariable int size) {
        return ResponseVO.ok(bookService.salesVolumeTop(size));
    }

    @GetMapping("recommend/{size}")
    public ResponseVO<List<Book>> recommend(@PathVariable int size) {
        if (CurrentUserThreadLocal.getCurrentUser() != null &&
            CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            return ResponseVO.ok(bookService.recommend(size));
        }
        return ResponseVO.ok(bookService.salesVolumeTop(size));
    }
}
