package com.project.platform.controller;

import com.project.platform.entity.BookCollect;
import com.project.platform.mapper.BookCollectMapper;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookCollect")
public class BookCollectController {
    @Resource
    private BookCollectMapper bookCollectMapper;

    @GetMapping("page")
    public ResponseVO<PageVO<BookCollect>> page(@RequestParam Map<String, Object> query,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<BookCollect> page = new PageVO<>();
        query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        List<BookCollect> list = bookCollectMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(bookCollectMapper.queryCount(query));
        return ResponseVO.ok(page);
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody BookCollect entity) {
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        bookCollectMapper.insert(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete/{id}")
    public ResponseVO delete(@PathVariable Integer id) {
        bookCollectMapper.deleteById(id);
        return ResponseVO.ok();
    }

    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            bookCollectMapper.deleteById(id);
        }
        return ResponseVO.ok();
    }
}
