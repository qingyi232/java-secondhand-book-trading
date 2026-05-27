package com.project.platform.entity;

import java.time.LocalDateTime;

public class ShoppingCart {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Integer quantity;
    private LocalDateTime createTime;

    private String productName;
    private String productMainImg;
    private Float productPrice;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductMainImg() { return productMainImg; }
    public void setProductMainImg(String productMainImg) { this.productMainImg = productMainImg; }
    public Float getProductPrice() { return productPrice; }
    public void setProductPrice(Float productPrice) { this.productPrice = productPrice; }
}
