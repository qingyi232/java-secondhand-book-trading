package com.project.platform.controller;

import com.project.platform.entity.BookOrder;
import com.project.platform.service.BookOrderService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bookOrder")
public class BookOrderController {
    @Resource
    private BookOrderService bookOrderService;

    @GetMapping("page")
    public ResponseVO<PageVO<BookOrder>> page(@RequestParam Map<String, Object> query,
                                              @RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseVO.ok(bookOrderService.page(query, pageNum, pageSize));
    }

    @PostMapping("create")
    public ResponseVO create(@RequestBody BookOrder order) {
        bookOrderService.createOrder(order);
        return ResponseVO.ok();
    }

    @PutMapping("pay/{id}")
    public ResponseVO pay(@PathVariable Integer id) {
        bookOrderService.pay(id);
        return ResponseVO.ok();
    }

    @PutMapping("ship/{id}")
    public ResponseVO ship(@PathVariable Integer id, @RequestParam String trackingNumber) {
        bookOrderService.ship(id, trackingNumber);
        return ResponseVO.ok();
    }

    @PutMapping("receive/{id}")
    public ResponseVO receive(@PathVariable Integer id) {
        bookOrderService.receive(id);
        return ResponseVO.ok();
    }

    @PutMapping("cancel/{id}")
    public ResponseVO cancel(@PathVariable Integer id) {
        bookOrderService.cancel(id);
        return ResponseVO.ok();
    }

    @PutMapping("applyRefund/{id}")
    public ResponseVO applyRefund(@PathVariable Integer id) {
        bookOrderService.applyRefund(id);
        return ResponseVO.ok();
    }

    @PutMapping("approveRefund/{id}")
    public ResponseVO approveRefund(@PathVariable Integer id) {
        bookOrderService.approveRefund(id);
        return ResponseVO.ok();
    }
}
