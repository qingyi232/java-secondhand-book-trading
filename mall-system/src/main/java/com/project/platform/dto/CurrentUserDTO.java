package com.project.platform.dto;

public class CurrentUserDTO {
    private Integer id;
    private String type;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String tel;
    private String email;
    private Float balance;
    private String name;
    private String aptitudeImgs;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Float getBalance() { return balance; }
    public void setBalance(Float balance) { this.balance = balance; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAptitudeImgs() { return aptitudeImgs; }
    public void setAptitudeImgs(String aptitudeImgs) { this.aptitudeImgs = aptitudeImgs; }
}
