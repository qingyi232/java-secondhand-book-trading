package com.project.platform.controller;

import com.project.platform.entity.Product;
import com.project.platform.entity.ProductCollect;
import com.project.platform.mapper.ProductCollectMapper;
import com.project.platform.mapper.ProductMapper;
import com.project.platform.service.ProductService;
import com.project.platform.service.RecommendationService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品信息
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Resource
    private ProductCollectMapper productCollectMapper;

    @Resource
    private RecommendationService recommendationService;

    /**
     * 分页查询
     *
     * @param query
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public ResponseVO<PageVO<Product>> page(@RequestParam Map<String, Object> query, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<Product> page = productService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById/{id}")
    public ResponseVO<Product> selectById(@PathVariable("id") Integer id) {
        Product entity = productService.selectById(id);
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            ProductCollect productCollect = productCollectMapper.selectByProductIdAndUserId(id, CurrentUserThreadLocal.getCurrentUser().getId());
            if (productCollect != null) {
                entity.setProductCollectId(productCollect.getId());
            }
        }
        return ResponseVO.ok(entity);
    }


    /**
     * 列表
     *
     * @return
     */
    @GetMapping("list")
    public ResponseVO<List<Product>> list() {
        return ResponseVO.ok(productService.list());
    }


    /**
     * 新增
     *
     * @param entity
     * @return
     */
    @PostMapping("add")
    public ResponseVO add(@RequestBody Product entity) {
        productService.insert(entity);
        return ResponseVO.ok();
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @PutMapping("update")
    public ResponseVO update(@RequestBody Product entity) {
        productService.updateById(entity);
        return ResponseVO.ok();
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("delBatch")
    public ResponseVO delBatch(@RequestBody List<Integer> ids) {
        productService.removeByIds(ids);
        return ResponseVO.ok();
    }

    /**
     * 审核商品（管理员通过/驳回）
     */
    @PutMapping("review/{id}")
    public ResponseVO review(@PathVariable Integer id, @RequestParam String reviewStatus) {
        productService.review(id, reviewStatus);
        return ResponseVO.ok();
    }

    @GetMapping("salesVolumeTop/{size}")
    public ResponseVO<List<Product>> salesVolumeTop(@PathVariable int size) {
        return ResponseVO.ok(productService.salesVolumeTop(size));
    }

    /**
     * 列表 - 基于协同过滤的推荐
     *
     * @return
     */
    @GetMapping("recommend/{size}")
    public ResponseVO<List<Product>> recommend(@PathVariable int size) {
        // 如果用户已登录，使用协同过滤推荐
        if (CurrentUserThreadLocal.getCurrentUser() != null &&
            CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            return ResponseVO.ok(recommendationService.recommendHybrid(
                CurrentUserThreadLocal.getCurrentUser().getId(), size));
        } else {
            // 未登录用户使用原来的推荐逻辑
            return ResponseVO.ok(productService.recommend(size));
        }
    }

    /**
     * 基于用户的协同过滤推荐
     */
    @GetMapping("recommend/user-based/{size}")
    public ResponseVO<List<Product>> recommendUserBased(@PathVariable int size) {
        if (CurrentUserThreadLocal.getCurrentUser() == null ||
            !CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            return ResponseVO.ok(productService.recommend(size));
        }
        return ResponseVO.ok(recommendationService.recommendByUserCollaborativeFiltering(
            CurrentUserThreadLocal.getCurrentUser().getId(), size));
    }

    /**
     * 基于物品的协同过滤推荐
     */
    @GetMapping("recommend/item-based/{size}")
    public ResponseVO<List<Product>> recommendItemBased(@PathVariable int size) {
        if (CurrentUserThreadLocal.getCurrentUser() == null ||
            !CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            return ResponseVO.ok(productService.recommend(size));
        }
        return ResponseVO.ok(recommendationService.recommendByItemCollaborativeFiltering(
            CurrentUserThreadLocal.getCurrentUser().getId(), size));
    }

    /**
     * 混合推荐算法
     */
    @GetMapping("recommend/hybrid/{size}")
    public ResponseVO<List<Product>> recommendHybrid(@PathVariable int size) {
        if (CurrentUserThreadLocal.getCurrentUser() == null ||
            !CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            return ResponseVO.ok(productService.recommend(size));
        }
        return ResponseVO.ok(recommendationService.recommendHybrid(
            CurrentUserThreadLocal.getCurrentUser().getId(), size));
    }

}
