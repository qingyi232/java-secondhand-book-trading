package com.project.platform.entity;

import java.time.LocalDateTime;

public class BookCollect {
    private Integer id;
    private Integer bookId;
    private String bookName;
    private String bookMainImg;
    private String bookPrice;
    private Integer userId;
    private LocalDateTime createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getBookMainImg() { return bookMainImg; }
    public void setBookMainImg(String bookMainImg) { this.bookMainImg = bookMainImg; }
    public String getBookPrice() { return bookPrice; }
    public void setBookPrice(String bookPrice) { this.bookPrice = bookPrice; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
