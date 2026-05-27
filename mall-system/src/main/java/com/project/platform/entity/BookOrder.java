package com.project.platform.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookOrder {
    private Integer id;
    private String orderNo;
    private Integer bookId;
    private String bookName;
    private String bookMainImg;
    private Integer shopId;
    private String shopName;
    private Integer userId;
    private String userName;
    private Integer quantity;
    private BigDecimal totalMoney;
    private Integer status;
    private String consigneeName;
    private String consigneeTel;
    private String consigneeAddress;
    private String trackingNumber;
    private String remark;
    private Integer orderEvaluateId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public String getBookMainImg() { return bookMainImg; }
    public void setBookMainImg(String bookMainImg) { this.bookMainImg = bookMainImg; }
    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getTotalMoney() { return totalMoney; }
    public void setTotalMoney(BigDecimal totalMoney) { this.totalMoney = totalMoney; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getConsigneeName() { return consigneeName; }
    public void setConsigneeName(String consigneeName) { this.consigneeName = consigneeName; }
    public String getConsigneeTel() { return consigneeTel; }
    public void setConsigneeTel(String consigneeTel) { this.consigneeTel = consigneeTel; }
    public String getConsigneeAddress() { return consigneeAddress; }
    public void setConsigneeAddress(String consigneeAddress) { this.consigneeAddress = consigneeAddress; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getOrderEvaluateId() { return orderEvaluateId; }
    public void setOrderEvaluateId(Integer orderEvaluateId) { this.orderEvaluateId = orderEvaluateId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
