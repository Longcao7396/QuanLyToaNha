package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model cho chủ hộ
 * Module 1: Quản lý cư dân & căn hộ
 * 2. Quản lý chủ hộ
 * Chủ hộ là người sở hữu căn hộ (có thể không trực tiếp ở)
 */
public class Owner {
    private Integer id;
    private String fullName; // Họ tên
    private String identityCard; // CMND/CCCD
    private String phone; // SĐT
    private String email; // Email
    private String contactAddress; // Địa chỉ liên hệ
    private String contractFilePath; // Đường dẫn file hợp đồng mua/bàn giao (PDF)
    private Integer userId; // Tài khoản cư dân (nếu chủ hộ dùng app)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Danh sách căn hộ đang sở hữu (không lưu trong DB, chỉ để hiển thị)
    private List<Apartment> ownedApartments;

    public Owner() {
    }

    public Owner(String fullName, String identityCard, String phone, String email) {
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContractFilePath() {
        return contractFilePath;
    }

    public void setContractFilePath(String contractFilePath) {
        this.contractFilePath = contractFilePath;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Apartment> getOwnedApartments() {
        return ownedApartments;
    }

    public void setOwnedApartments(List<Apartment> ownedApartments) {
        this.ownedApartments = ownedApartments;
    }
}

