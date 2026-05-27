package com.project.platform.service.impl;

import com.project.platform.entity.Book;
import com.project.platform.entity.BookOrder;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.BookMapper;
import com.project.platform.mapper.BookOrderMapper;
import com.project.platform.service.BookOrderService;
import com.project.platform.service.BookService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookOrderServiceImpl implements BookOrderService {
    @Resource
    private BookOrderMapper bookOrderMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BookService bookService;

    private static final Map<Integer, List<Integer>> LEGAL_TRANSITIONS = new HashMap<>();
    static {
        LEGAL_TRANSITIONS.put(0, Arrays.asList(1, 4));
        LEGAL_TRANSITIONS.put(1, Arrays.asList(2, 5));
        LEGAL_TRANSITIONS.put(2, Arrays.asList(3, 5));
        LEGAL_TRANSITIONS.put(3, Collections.emptyList());
        LEGAL_TRANSITIONS.put(4, Collections.emptyList());
        LEGAL_TRANSITIONS.put(5, Collections.singletonList(6));
        LEGAL_TRANSITIONS.put(6, Collections.emptyList());
    }

    private boolean checkTransition(Integer from, Integer to) {
        List<Integer> allowed = LEGAL_TRANSITIONS.get(from);
        return allowed != null && allowed.contains(to);
    }

    private void updateStatus(Integer orderId, Integer targetStatus) {
        BookOrder order = bookOrderMapper.selectById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (!checkTransition(order.getStatus(), targetStatus)) {
            throw new CustomException("订单状态不允许此操作");
        }
        order.setStatus(targetStatus);
        bookOrderMapper.updateById(order);
    }

    @Override
    public PageVO<BookOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<BookOrder> page = new PageVO<>();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("SHOP")) {
            query.put("shopId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<BookOrder> list = bookOrderMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(bookOrderMapper.queryCount(query));
        return page;
    }

    @Override
    public void createOrder(BookOrder order) {
        Book book = bookMapper.selectById(order.getBookId());
        if (book == null) throw new CustomException("书籍不存在");
        if (book.getStock() < order.getQuantity()) throw new CustomException("库存不足");

        String orderNo = "BO" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", new Random().nextInt(10000));

        order.setOrderNo(orderNo);
        order.setShopId(book.getShopId());
        order.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        order.setTotalMoney(book.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
        order.setStatus(0);
        bookOrderMapper.insert(order);
    }

    @Override
    public void pay(Integer orderId) {
        updateStatus(orderId, 1);
        BookOrder order = bookOrderMapper.selectById(orderId);
        bookService.updateStock(order.getBookId(), order.getQuantity(), false);
    }

    @Override
    public void ship(Integer orderId, String trackingNumber) {
        BookOrder order = bookOrderMapper.selectById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (!checkTransition(order.getStatus(), 2)) throw new CustomException("订单状态不允许发货");
        order.setStatus(2);
        order.setTrackingNumber(trackingNumber);
        bookOrderMapper.updateById(order);
    }

    @Override
    public void receive(Integer orderId) {
        updateStatus(orderId, 3);
    }

    @Override
    public void cancel(Integer orderId) {
        BookOrder order = bookOrderMapper.selectById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (order.getStatus() == 1) {
            bookService.updateStock(order.getBookId(), order.getQuantity(), true);
        }
        if (!checkTransition(order.getStatus(), 4)) throw new CustomException("订单状态不允许取消");
        order.setStatus(4);
        bookOrderMapper.updateById(order);
    }

    @Override
    public void applyRefund(Integer orderId) {
        updateStatus(orderId, 5);
    }

    @Override
    public void approveRefund(Integer orderId) {
        BookOrder order = bookOrderMapper.selectById(orderId);
        if (order == null) throw new CustomException("订单不存在");
        if (!checkTransition(order.getStatus(), 6)) throw new CustomException("订单状态不允许退款");
        bookService.updateStock(order.getBookId(), order.getQuantity(), true);
        order.setStatus(6);
        bookOrderMapper.updateById(order);
    }
}
