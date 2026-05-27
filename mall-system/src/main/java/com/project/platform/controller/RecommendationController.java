package com.project.platform.controller;

import com.project.platform.entity.Product;
import com.project.platform.service.ProductService;
import com.project.platform.service.RecommendationService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 推荐控制器 - 处理不重复的推荐逻辑
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    @Resource
    private RecommendationService recommendationService;

    @Resource
    private ProductService productService;

    /**
     * 获取首页推荐数据（智能推荐 + 普通推荐，确保不重复）
     */
    @GetMapping("homepage")
    public ResponseVO<Map<String, Object>> getHomepageRecommendations(
            @RequestParam(defaultValue = "hybrid") String collaborativeType,
            @RequestParam(defaultValue = "12") Integer collaborativeSize,
            @RequestParam(defaultValue = "3") Integer normalSize) {

        Map<String, Object> result = new HashMap<>();
        List<Product> collaborativeRecommendations;
        List<Product> normalRecommendations;

        // 1. 先获取协同过滤推荐
        if (CurrentUserThreadLocal.getCurrentUser() != null &&
            CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();

            switch (collaborativeType) {
                case "user-based":
                    collaborativeRecommendations = recommendationService.recommendByUserCollaborativeFiltering(userId, collaborativeSize);
                    break;
                case "item-based":
                    collaborativeRecommendations = recommendationService.recommendByItemCollaborativeFiltering(userId, collaborativeSize);
                    break;
                case "hybrid":
                default:
                    collaborativeRecommendations = recommendationService.recommendHybrid(userId, collaborativeSize);
                    break;
            }
        } else {
            // 未登录用户使用热门商品
            collaborativeRecommendations = productService.salesVolumeTop(collaborativeSize);
        }

        // 2. 获取协同过滤推荐的商品ID列表
        List<Integer> excludeIds = collaborativeRecommendations.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        // 3. 获取普通推荐，排除协同过滤推荐的商品
        if (CurrentUserThreadLocal.getCurrentUser() != null &&
            CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
            normalRecommendations = getFilteredNormalRecommendations(userId, normalSize, excludeIds);
        } else {
            normalRecommendations = getFilteredHotProducts(normalSize, excludeIds);
        }

        result.put("collaborativeRecommendations", collaborativeRecommendations);
        result.put("normalRecommendations", normalRecommendations);

        return ResponseVO.ok(result);
    }

    /**
     * 获取过滤后的普通推荐（基于原来的推荐逻辑）
     */
    private List<Product> getFilteredNormalRecommendations(Integer userId, Integer size, List<Integer> excludeIds) {
        // 使用原来的推荐逻辑，但要过滤掉已推荐的商品
        List<Product> allRecommendations = productService.recommend(size * 3); // 获取更多商品以便过滤

        List<Product> filteredRecommendations = allRecommendations.stream()
                .filter(product -> !excludeIds.contains(product.getId()))
                .limit(size)
                .collect(Collectors.toList());

        // 如果过滤后数量不够，用热门商品补充
        if (filteredRecommendations.size() < size) {
            List<Product> hotProducts = getFilteredHotProducts(size - filteredRecommendations.size(), excludeIds);
            filteredRecommendations.addAll(hotProducts);
        }

        // 如果仍然数量不足，允许重复，从原推荐列表中补充
        if (filteredRecommendations.size() < size) {
            List<Product> duplicateRecommendations = allRecommendations.stream()
                    .filter(product -> excludeIds.contains(product.getId())) // 获取被排除的商品
                    .limit(size - filteredRecommendations.size())
                    .collect(Collectors.toList());
            filteredRecommendations.addAll(duplicateRecommendations);
        }

        return filteredRecommendations.stream().limit(size).collect(Collectors.toList());
    }

    /**
     * 获取过滤后的热门商品
     */
    private List<Product> getFilteredHotProducts(Integer size, List<Integer> excludeIds) {
        List<Product> hotProducts = productService.salesVolumeTop(size * 3);
        List<Product> filteredProducts = hotProducts.stream()
                .filter(product -> !excludeIds.contains(product.getId()))
                .limit(size)
                .collect(Collectors.toList());

        // 如果过滤后的热门商品数量不足，允许重复
        if (filteredProducts.size() < size && !hotProducts.isEmpty()) {
            List<Product> duplicateProducts = hotProducts.stream()
                    .filter(product -> excludeIds.contains(product.getId())) // 获取被排除的商品
                    .limit(size - filteredProducts.size())
                    .collect(Collectors.toList());
            filteredProducts.addAll(duplicateProducts);
        }

        return filteredProducts;
    }
}