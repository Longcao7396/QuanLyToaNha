package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho thẻ vào - ra
 * Module 1: Quản lý cư dân & căn hộ
 * 5. Quản lý thẻ vào – ra (nếu tòa nhà dùng thẻ cư dân/QR)
 */
public class AccessCard {
    private Integer id;
    private String cardCode; // Mã thẻ / RFID / QR
    private Integer residentId; // Cư dân được cấp thẻ
    private String cardType; // Loại thẻ: RFID, QR_CODE, VÂN_TAY, FACE_ID, NFC_APP
    private String residentType; // Cư dân / người thân / giúp việc
    private String accessRights; // Quyền truy cập: block, tầng, khu tiện ích (JSON hoặc text)
    private String status; // Trạng thái: ĐANG_HOẠT_ĐỘNG, VÔ_HIỆU, BÁO_MẤT
    private LocalDateTime issuedDate; // Ngày cấp
    private Integer issuedBy; // Người cấp (user_id)
    private LocalDateTime expiryDate; // Ngày hết hạn (nếu có)
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccessCard() {
    }

    public AccessCard(String cardCode, Integer residentId, String cardType) {
        this.cardCode = cardCode;
        this.residentId = residentId;
        this.cardType = cardType;
        this.status = "ĐANG_HOẠT_ĐỘNG";
        this.issuedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getResidentType() {
        return residentType;
    }

    public void setResidentType(String residentType) {
        this.residentType = residentType;
    }

    public Integer getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(Integer issuedBy) {
        this.issuedBy = issuedBy;
    }

    // Helper methods
    public boolean isActive() {
        return "ĐANG_HOẠT_ĐỘNG".equals(status);
    }

    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiryDate);
    }
}

