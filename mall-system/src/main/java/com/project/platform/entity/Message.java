package com.project.platform.entity;

import java.time.LocalDateTime;

public class Message {
    private Integer id;
    private Integer fromUserId;
    private String fromUserType;
    private String fromUserName;
    private String fromUserAvatar;
    private Integer toUserId;
    private String toUserType;
    private String content;
    private Integer isRead;
    private Integer bookId;
    private String bookName;
    private LocalDateTime createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFromUserId() { return fromUserId; }
    public void setFromUserId(Integer fromUserId) { this.fromUserId = fromUserId; }
    public String getFromUserType() { return fromUserType; }
    public void setFromUserType(String fromUserType) { this.fromUserType = fromUserType; }
    public String getFromUserName() { return fromUserName; }
    public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
    public String getFromUserAvatar() { return fromUserAvatar; }
    public void setFromUserAvatar(String fromUserAvatar) { this.fromUserAvatar = fromUserAvatar; }
    public Integer getToUserId() { return toUserId; }
    public void setToUserId(Integer toUserId) { this.toUserId = toUserId; }
    public String getToUserType() { return toUserType; }
    public void setToUserType(String toUserType) { this.toUserType = toUserType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
