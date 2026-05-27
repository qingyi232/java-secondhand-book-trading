package com.project.platform.service.impl;

import com.project.platform.entity.Book;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.BookCollectMapper;
import com.project.platform.mapper.BookMapper;
import com.project.platform.mapper.BrowsingHistoryMapper;
import com.project.platform.service.BookService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ValueNameVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;
    @Resource
    private BookCollectMapper bookCollectMapper;
    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Override
    public PageVO<Book> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Book> page = new PageVO<>();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("SHOP")) {
            query.put("shopId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("onlyApproved", "true");
        }
        List<Book> list = bookMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(bookMapper.queryCount(query));
        return page;
    }

    @Override
    public Book selectById(Integer id) {
        return bookMapper.selectById(id);
    }

    @Override
    public List<Book> list() {
        return bookMapper.list();
    }

    @Override
    public void insert(Book entity) {
        if (entity.getBookName() == null || entity.getBookName().trim().isEmpty()) {
            throw new CustomException("书名不能为空");
        }
        if (entity.getPrice() == null) {
            throw new CustomException("售价不能为空");
        }
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("SHOP")) {
            throw new CustomException("当前用户不是卖家，只有卖家才允许发布书籍");
        }
        entity.setShopId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setSalesVolume(0);
        entity.setReviewStatus("待审核");
        entity.setStatus("上架");
        bookMapper.insert(entity);
    }

    @Override
    public void updateById(Book entity) {
        bookMapper.updateById(entity);
    }

    @Override
    public void removeByIds(List<Integer> ids) {
        bookMapper.removeByIds(ids);
    }

    @Override
    public void review(Integer id, String reviewStatus) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new CustomException("书籍不存在");
        }
        if (!"已通过".equals(reviewStatus) && !"已驳回".equals(reviewStatus)) {
            throw new CustomException("审核状态无效");
        }
        book.setReviewStatus(reviewStatus);
        bookMapper.updateById(book);
    }

    @Override
    public List<Book> salesVolumeTop(int size) {
        return bookMapper.salesVolumeTop(size);
    }

    @Override
    public List<Book> recommend(Integer size) {
        List<Book> bookList = list();
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        List<ValueNameVO> browsingStats = browsingHistoryMapper.statisticsCategoryIdByUserId(userId);
        List<ValueNameVO> collectStats = bookCollectMapper.statisticsCategoryIdByUserId(userId);

        for (Book book : bookList) {
            for (ValueNameVO item : browsingStats) {
                if (item.getName().equals(book.getCategoryId())) {
                    book.setWeight(book.getWeight() + 1);
                }
            }
            for (ValueNameVO item : collectStats) {
                if (item.getName().equals(book.getCategoryId())) {
                    book.setWeight(book.getWeight() + 1);
                }
            }
        }
        return bookList.stream()
                .sorted(Comparator.comparing(Book::getWeight).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStock(Integer id, Integer quantity, boolean isIncrease) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new CustomException("书籍不存在");
        }
        if (isIncrease) {
            book.setStock(book.getStock() + quantity);
            book.setSalesVolume(book.getSalesVolume() - quantity);
        } else {
            if (book.getStock() < quantity) {
                throw new CustomException("库存不足");
            }
            book.setStock(book.getStock() - quantity);
            book.setSalesVolume(book.getSalesVolume() + quantity);
        }
        bookMapper.updateById(book);
    }
}
