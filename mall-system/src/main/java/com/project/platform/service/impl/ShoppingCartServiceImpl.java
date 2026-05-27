package com.project.platform.service.impl;

import com.project.platform.dto.CreateOrderByShoppingCartDTO;
import com.project.platform.entity.Book;
import com.project.platform.entity.BookOrder;
import com.project.platform.entity.ShoppingCart;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.BookMapper;
import com.project.platform.mapper.ShoppingCartMapper;
import com.project.platform.service.BookOrderService;
import com.project.platform.service.ShoppingCartService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.project.platform.vo.PageVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private BookOrderService bookOrderService;
    @Resource
    private BookMapper bookMapper;

    @Override
    public PageVO<ShoppingCart> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<ShoppingCart> page = new PageVO<>();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        List<ShoppingCart> list = shoppingCartMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(shoppingCartMapper.queryCount(query));
        return page;
    }

    @Override
    public ShoppingCart selectById(Integer id) {
        return shoppingCartMapper.selectById(id);
    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list();
    }

    @Override
    public void insert(ShoppingCart entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            throw new CustomException("只有买家才能添加书籍到购物车");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        ShoppingCart existing = shoppingCartMapper.selectByBookIdAndUserId(entity.getBookId(), entity.getUserId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + entity.getQuantity());
            shoppingCartMapper.updateById(existing);
            return;
        }
        shoppingCartMapper.insert(entity);
    }

    @Override
    public void updateById(ShoppingCart entity) {
        shoppingCartMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        shoppingCartMapper.removeByIds(ids);
    }

    @Override
    public void createOrder(CreateOrderByShoppingCartDTO dto) {
        List<String> errorMessages = new ArrayList<>();
        dto.getIds().forEach(cartId -> {
            ShoppingCart cart = shoppingCartMapper.selectById(cartId);
            if (cart == null) return;
            BookOrder order = new BookOrder();
            order.setBookId(cart.getBookId());
            order.setQuantity(cart.getQuantity());
            order.setConsigneeAddress(dto.getConsigneeAddress());
            order.setConsigneeName(dto.getConsigneeName());
            order.setConsigneeTel(dto.getConsigneeTel());
            order.setRemark(dto.getRemark());
            try {
                bookOrderService.createOrder(order);
                shoppingCartMapper.removeByIds(List.of(cartId));
            } catch (CustomException e) {
                Book book = bookMapper.selectById(cart.getBookId());
                errorMessages.add(book.getBookName() + " 下单失败：" + e.getMessage());
            }
        });
        if (!errorMessages.isEmpty()) {
            throw new CustomException(String.join("；", errorMessages));
        }
    }
}
