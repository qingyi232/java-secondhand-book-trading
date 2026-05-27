package com.project.platform.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Book {
    private Integer id;
    private String bookName;
    private String author;
    private String publisher;
    private String isbn;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private String quality;
    private Integer categoryId;
    private String categoryName;
    private String mainImg;
    private String imgList;
    private String intro;
    private Integer stock;
    private Integer salesVolume;
    private Integer shopId;
    private String shopName;
    private String reviewStatus;
    private String status;
    private LocalDateTime createTime;

    private Integer bookCollectId;
    private int weight;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getMainImg() { return mainImg; }
    public void setMainImg(String mainImg) { this.mainImg = mainImg; }
    public String getImgList() { return imgList; }
    public void setImgList(String imgList) { this.imgList = imgList; }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getSalesVolume() { return salesVolume; }
    public void setSalesVolume(Integer salesVolume) { this.salesVolume = salesVolume; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(String reviewStatus) { this.reviewStatus = reviewStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Integer getBookCollectId() { return bookCollectId; }
    public void setBookCollectId(Integer bookCollectId) { this.bookCollectId = bookCollectId; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}
