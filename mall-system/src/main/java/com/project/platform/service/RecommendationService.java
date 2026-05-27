package com.project.platform.service;

import com.project.platform.entity.Product;

import java.util.List;

/**
 * 协同过滤推荐服务接口
 */
public interface RecommendationService {

    /**
     * 基于用户的协同过滤推荐
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @return 推荐商品列表
     */
    List<Product> recommendByUserCollaborativeFiltering(Integer userId, Integer size);

    /**
     * 基于物品的协同过滤推荐
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @return 推荐商品列表
     */
    List<Product> recommendByItemCollaborativeFiltering(Integer userId, Integer size);

    /**
     * 混合推荐算法
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @return 推荐商品列表
     */
    List<Product> recommendHybrid(Integer userId, Integer size);

    /**
     * 基于用户的协同过滤推荐（排除指定商品）
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @param excludeProductIds 要排除的商品ID列表
     * @return 推荐商品列表
     */
    List<Product> recommendByUserCollaborativeFiltering(Integer userId, Integer size, List<Integer> excludeProductIds);

    /**
     * 基于物品的协同过滤推荐（排除指定商品）
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @param excludeProductIds 要排除的商品ID列表
     * @return 推荐商品列表
     */
    List<Product> recommendByItemCollaborativeFiltering(Integer userId, Integer size, List<Integer> excludeProductIds);

    /**
     * 混合推荐算法（排除指定商品）
     * @param userId 用户ID
     * @param size 推荐商品数量
     * @param excludeProductIds 要排除的商品ID列表
     * @return 推荐商品列表
     */
    List<Product> recommendHybrid(Integer userId, Integer size, List<Integer> excludeProductIds);
}