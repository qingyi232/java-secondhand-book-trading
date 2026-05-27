package com.project.platform.service.impl;

import com.project.platform.entity.Product;
import com.project.platform.mapper.ProductMapper;
import com.project.platform.mapper.ProductBrowsingHistoryMapper;
import com.project.platform.mapper.ProductCollectMapper;
import com.project.platform.mapper.ProductOrderMapper;
import com.project.platform.mapper.ProductOrderEvaluateMapper;
import com.project.platform.service.RecommendationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 协同过滤推荐服务实现
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductBrowsingHistoryMapper productBrowsingHistoryMapper;

    @Resource
    private ProductCollectMapper productCollectMapper;

    @Resource
    private ProductOrderMapper productOrderMapper;

    @Resource
    private ProductOrderEvaluateMapper productOrderEvaluateMapper;

    @Override
    public List<Product> recommendByUserCollaborativeFiltering(Integer userId, Integer size) {
        return recommendByUserCollaborativeFiltering(userId, size, new ArrayList<>());
    }

    @Override
    public List<Product> recommendByUserCollaborativeFiltering(Integer userId, Integer size, List<Integer> excludeProductIds) {
        // 1. 构建用户-商品评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix = buildUserItemMatrix();

        // 2. 如果当前用户没有任何行为记录，返回热门商品
        if (!userItemMatrix.containsKey(userId)) {
            return getFilteredHotProducts(size, excludeProductIds);
        }

        // 3. 计算用户相似度
        Map<Integer, Double> userSimilarities = calculateUserSimilarities(userId, userItemMatrix);

        // 4. 基于相似用户生成推荐
        List<Product> recommendations = generateUserBasedRecommendations(userId, userSimilarities, userItemMatrix, size, excludeProductIds);

        return recommendations;
    }

    @Override
    public List<Product> recommendByItemCollaborativeFiltering(Integer userId, Integer size) {
        return recommendByItemCollaborativeFiltering(userId, size, new ArrayList<>());
    }

    @Override
    public List<Product> recommendByItemCollaborativeFiltering(Integer userId, Integer size, List<Integer> excludeProductIds) {
        // 1. 构建用户-商品评分矩阵
        Map<Integer, Map<Integer, Double>> userItemMatrix = buildUserItemMatrix();

        if (!userItemMatrix.containsKey(userId)) {
            return getFilteredHotProducts(size, excludeProductIds);
        }

        // 2. 构建商品-用户矩阵
        Map<Integer, Map<Integer, Double>> itemUserMatrix = buildItemUserMatrix(userItemMatrix);

        // 3. 计算商品相似度
        Map<Integer, Double> itemSimilarities = new HashMap<>();
        Set<Integer> userItems = userItemMatrix.get(userId).keySet();

        for (Integer userItem : userItems) {
            for (Integer otherItem : itemUserMatrix.keySet()) {
                if (!userItems.contains(otherItem) && !excludeProductIds.contains(otherItem)) {
                    double similarity = calculateItemSimilarity(userItem, otherItem, itemUserMatrix);
                    itemSimilarities.put(otherItem, itemSimilarities.getOrDefault(otherItem, 0.0) + similarity);
                }
            }
        }

        // 4. 获取推荐商品
        List<Integer> recommendedItemIds = itemSimilarities.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedItemIds.stream()
                .map(productMapper::selectById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> recommendHybrid(Integer userId, Integer size) {
        return recommendHybrid(userId, size, new ArrayList<>());
    }

    @Override
    public List<Product> recommendHybrid(Integer userId, Integer size, List<Integer> excludeProductIds) {
        // 混合推荐：结合用户协同过滤和物品协同过滤
        int userBasedSize = size / 2;
        int itemBasedSize = size - userBasedSize;

        List<Product> userBasedRecommendations = recommendByUserCollaborativeFiltering(userId, userBasedSize, excludeProductIds);
        List<Product> itemBasedRecommendations = recommendByItemCollaborativeFiltering(userId, itemBasedSize, excludeProductIds);

        // 合并去重
        Set<Integer> addedIds = new HashSet<>(excludeProductIds);
        List<Product> finalRecommendations = new ArrayList<>();

        for (Product product : userBasedRecommendations) {
            if (!addedIds.contains(product.getId())) {
                finalRecommendations.add(product);
                addedIds.add(product.getId());
            }
        }

        for (Product product : itemBasedRecommendations) {
            if (!addedIds.contains(product.getId()) && finalRecommendations.size() < size) {
                finalRecommendations.add(product);
                addedIds.add(product.getId());
            }
        }

        // 如果推荐数量不足，用热门商品补充
        if (finalRecommendations.size() < size) {
            List<Product> hotProducts = getFilteredHotProducts(size * 2, new ArrayList<>(addedIds));
            for (Product product : hotProducts) {
                if (!addedIds.contains(product.getId()) && finalRecommendations.size() < size) {
                    finalRecommendations.add(product);
                    addedIds.add(product.getId());
                }
            }
        }

        // 如果仍然数量不足，允许重复推荐
        if (finalRecommendations.size() < size) {
            List<Product> allProducts = productMapper.salesVolumeTop(size * 3);
            for (Product product : allProducts) {
                if (finalRecommendations.size() >= size) break;
                finalRecommendations.add(product); // 不再检查重复，直接添加
            }
        }

        return finalRecommendations;
    }

    /**
     * 构建用户-商品评分矩阵
     * 评分规则：浏览=1分，收藏=2分，购买=3分，评价=评价分数
     */
    private Map<Integer, Map<Integer, Double>> buildUserItemMatrix() {
        Map<Integer, Map<Integer, Double>> matrix = new HashMap<>();

        // 1. 浏览记录 - 1分
        List<Map<String, Object>> browsingHistory = productBrowsingHistoryMapper.selectAllUserProductPairs();
        for (Map<String, Object> record : browsingHistory) {
            Integer userId = (Integer) record.get("user_id");
            Integer productId = (Integer) record.get("product_id");
            matrix.computeIfAbsent(userId, k -> new HashMap<>())
                  .put(productId, matrix.get(userId).getOrDefault(productId, 0.0) + 1.0);
        }

        // 2. 收藏记录 - 2分
        List<Map<String, Object>> collectHistory = productCollectMapper.selectAllUserProductPairs();
        for (Map<String, Object> record : collectHistory) {
            Integer userId = (Integer) record.get("user_id");
            Integer productId = (Integer) record.get("product_id");
            matrix.computeIfAbsent(userId, k -> new HashMap<>())
                  .put(productId, matrix.get(userId).getOrDefault(productId, 0.0) + 2.0);
        }

        // 3. 购买记录 - 3分
        List<Map<String, Object>> orderHistory = productOrderMapper.selectAllUserProductPairs();
        for (Map<String, Object> record : orderHistory) {
            Integer userId = (Integer) record.get("user_id");
            Integer productId = (Integer) record.get("product_id");
            matrix.computeIfAbsent(userId, k -> new HashMap<>())
                  .put(productId, matrix.get(userId).getOrDefault(productId, 0.0) + 3.0);
        }

        // 4. 评价记录 - 评价分数
        List<Map<String, Object>> evaluateHistory = productOrderEvaluateMapper.selectAllUserProductRates();
        for (Map<String, Object> record : evaluateHistory) {
            Integer userId = (Integer) record.get("user_id");
            Integer productId = (Integer) record.get("product_id");
            Integer rate = (Integer) record.get("rate");
            matrix.computeIfAbsent(userId, k -> new HashMap<>())
                  .put(productId, matrix.get(userId).getOrDefault(productId, 0.0) + rate);
        }

        return matrix;
    }

    /**
     * 构建商品-用户矩阵
     */
    private Map<Integer, Map<Integer, Double>> buildItemUserMatrix(Map<Integer, Map<Integer, Double>> userItemMatrix) {
        Map<Integer, Map<Integer, Double>> itemUserMatrix = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Double>> userEntry : userItemMatrix.entrySet()) {
            Integer userId = userEntry.getKey();
            for (Map.Entry<Integer, Double> itemEntry : userEntry.getValue().entrySet()) {
                Integer itemId = itemEntry.getKey();
                Double rating = itemEntry.getValue();
                itemUserMatrix.computeIfAbsent(itemId, k -> new HashMap<>()).put(userId, rating);
            }
        }

        return itemUserMatrix;
    }

    /**
     * 计算用户之间的余弦相似度
     */
    private Map<Integer, Double> calculateUserSimilarities(Integer targetUserId, Map<Integer, Map<Integer, Double>> userItemMatrix) {
        Map<Integer, Double> similarities = new HashMap<>();
        Map<Integer, Double> targetUserRatings = userItemMatrix.get(targetUserId);

        for (Map.Entry<Integer, Map<Integer, Double>> entry : userItemMatrix.entrySet()) {
            Integer userId = entry.getKey();
            if (userId.equals(targetUserId)) continue;

            Map<Integer, Double> userRatings = entry.getValue();
            double similarity = calculateCosineSimilarity(targetUserRatings, userRatings);
            if (similarity > 0) {
                similarities.put(userId, similarity);
            }
        }

        return similarities;
    }

    /**
     * 计算商品之间的余弦相似度
     */
    private double calculateItemSimilarity(Integer item1, Integer item2, Map<Integer, Map<Integer, Double>> itemUserMatrix) {
        Map<Integer, Double> item1Ratings = itemUserMatrix.get(item1);
        Map<Integer, Double> item2Ratings = itemUserMatrix.get(item2);
        return calculateCosineSimilarity(item1Ratings, item2Ratings);
    }

    /**
     * 计算余弦相似度
     */
    private double calculateCosineSimilarity(Map<Integer, Double> ratingsA, Map<Integer, Double> ratingsB) {
        Set<Integer> commonItems = new HashSet<>(ratingsA.keySet());
        commonItems.retainAll(ratingsB.keySet());

        if (commonItems.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (Integer itemId : commonItems) {
            double ratingA = ratingsA.get(itemId);
            double ratingB = ratingsB.get(itemId);
            dotProduct += ratingA * ratingB;
        }

        for (double rating : ratingsA.values()) {
            normA += Math.pow(rating, 2);
        }

        for (double rating : ratingsB.values()) {
            normB += Math.pow(rating, 2);
        }

        if (normA == 0.0 || normB == 0.0) return 0.0;

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * 基于用户相似度生成推荐
     */
    private List<Product> generateUserBasedRecommendations(Integer userId, Map<Integer, Double> userSimilarities,
                                                          Map<Integer, Map<Integer, Double>> userItemMatrix, Integer size) {
        return generateUserBasedRecommendations(userId, userSimilarities, userItemMatrix, size, new ArrayList<>());
    }

    /**
     * 基于用户相似度生成推荐（排除指定商品）
     */
    private List<Product> generateUserBasedRecommendations(Integer userId, Map<Integer, Double> userSimilarities,
                                                          Map<Integer, Map<Integer, Double>> userItemMatrix, Integer size, List<Integer> excludeProductIds) {
        Map<Integer, Double> itemScores = new HashMap<>();
        Map<Integer, Double> userRatings = userItemMatrix.get(userId);

        // 计算每个商品的推荐分数
        for (Map.Entry<Integer, Double> similarUser : userSimilarities.entrySet()) {
            Integer similarUserId = similarUser.getKey();
            Double similarity = similarUser.getValue();

            Map<Integer, Double> similarUserRatings = userItemMatrix.get(similarUserId);
            for (Map.Entry<Integer, Double> itemEntry : similarUserRatings.entrySet()) {
                Integer itemId = itemEntry.getKey();
                Double rating = itemEntry.getValue();

                // 只推荐用户没有交互过的商品，且不在排除列表中
                if (!userRatings.containsKey(itemId) && !excludeProductIds.contains(itemId)) {
                    itemScores.put(itemId, itemScores.getOrDefault(itemId, 0.0) + similarity * rating);
                }
            }
        }

        // 获取评分最高的商品
        List<Integer> recommendedItemIds = itemScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(size)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedItemIds.stream()
                .map(productMapper::selectById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取过滤后的热门商品
     */
    private List<Product> getFilteredHotProducts(Integer size, List<Integer> excludeProductIds) {
        List<Product> hotProducts = productMapper.salesVolumeTop(size * 3); // 获取更多商品以便过滤
        List<Product> filteredProducts = hotProducts.stream()
                .filter(product -> !excludeProductIds.contains(product.getId()))
                .limit(size)
                .collect(Collectors.toList());

        // 如果过滤后的热门商品数量不足，允许重复
        if (filteredProducts.size() < size && !hotProducts.isEmpty()) {
            List<Product> duplicateProducts = hotProducts.stream()
                    .filter(product -> excludeProductIds.contains(product.getId())) // 获取被排除的商品
                    .limit(size - filteredProducts.size())
                    .collect(Collectors.toList());
            filteredProducts.addAll(duplicateProducts);

            // 如果仍然不足，重复使用现有商品
            while (filteredProducts.size() < size && !hotProducts.isEmpty()) {
                for (Product product : hotProducts) {
                    if (filteredProducts.size() >= size) break;
                    filteredProducts.add(product);
                }
            }
        }

        return filteredProducts.stream().limit(size).collect(Collectors.toList());
    }
}