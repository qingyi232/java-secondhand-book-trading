package com.project.platform.controller;

import com.project.platform.entity.BookOrder;
import com.project.platform.entity.OrderEvaluate;
import com.project.platform.mapper.BookOrderMapper;
import com.project.platform.mapper.OrderEvaluateMapper;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderEvaluate")
public class OrderEvaluateController {
    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;
    @Resource
    private BookOrderMapper bookOrderMapper;

    @GetMapping("page")
    public ResponseVO<PageVO<OrderEvaluate>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<OrderEvaluate> page = new PageVO<>();
        Map<String, Object> query = new HashMap<>();
        List<OrderEvaluate> list = orderEvaluateMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(orderEvaluateMapper.queryCount(query));
        return ResponseVO.ok(page);
    }

    @GetMapping("selectById/{id}")
    public ResponseVO<OrderEvaluate> selectById(@PathVariable Integer id) {
        return ResponseVO.ok(orderEvaluateMapper.selectById(id));
    }

    @GetMapping("listByBookId/{bookId}")
    public ResponseVO<List<OrderEvaluate>> listByBookId(@PathVariable Integer bookId) {
        return ResponseVO.ok(orderEvaluateMapper.selectByBookId(bookId));
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody OrderEvaluate entity) {
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        orderEvaluateMapper.insert(entity);
        if (entity.getOrderId() != null) {
            BookOrder order = bookOrderMapper.selectById(entity.getOrderId());
            if (order != null) {
                order.setOrderEvaluateId(entity.getId());
                bookOrderMapper.updateById(order);
            }
        }
        return ResponseVO.ok();
    }
}
