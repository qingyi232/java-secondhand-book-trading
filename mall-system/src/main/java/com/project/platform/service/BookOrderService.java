package com.project.platform.service;

import com.project.platform.entity.BookOrder;
import com.project.platform.vo.PageVO;

import java.util.Map;

public interface BookOrderService {
    PageVO<BookOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize);
    void createOrder(BookOrder order);
    void pay(Integer orderId);
    void ship(Integer orderId, String trackingNumber);
    void receive(Integer orderId);
    void cancel(Integer orderId);
    void applyRefund(Integer orderId);
    void approveRefund(Integer orderId);
}
