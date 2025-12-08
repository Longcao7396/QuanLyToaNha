package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho quản lý khách ra vào (Visitor Management)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class Visitor {
    private Integer id;
    private String visitorCode; // Mã khách / Mã QR
    private String fullName; // Họ tên
    private String identityCard; // CCCD/ảnh chụp (optional)
    private String identityImagePath; // Đường dẫn ảnh CCCD
    private Integer invitedBy; // Người mời (resident_id hoặc user_id)
    private String visitorType; // Loại khách: VÃNG_LAI, Ở_LẠI_DÀI_NGÀY, GIAO_HÀNG, NHÀ_THẦU
    private String purpose; // Mục đích
    private Integer apartmentId; // Căn hộ được phép đến
    private LocalDateTime entryTime; // Thời gian vào
    private LocalDateTime exitTime; // Thời gian ra
    private String licensePlate; // Biển số xe (nếu gửi xe)
    private String qrCode; // Mã QR đã cấp
    private LocalDateTime qrExpiryTime; // Thời gian hết hạn QR
    private Boolean isSingleUse; // Mã chỉ dùng 1 lần
    private String status; // Trạng thái: CHỜ_VÀO, ĐANG_TRONG_TÒA, ĐÃ_RA, HẾT_HẠN
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Visitor() {
    }

    public Visitor(String fullName, Integer invitedBy, String visitorType, Integer apartmentId) {
        this.fullName = fullName;
        this.invitedBy = invitedBy;
        this.visitorType = visitorType;
        this.apartmentId = apartmentId;
        this.status = "CHỜ_VÀO";
        this.isSingleUse = true;
        this.entryTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVisitorCode() {
        return visitorCode;
    }

    public void setVisitorCode(String visitorCode) {
        this.visitorCode = visitorCode;
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

    public String getIdentityImagePath() {
        return identityImagePath;
    }

    public void setIdentityImagePath(String identityImagePath) {
        this.identityImagePath = identityImagePath;
    }

    public Integer getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(Integer invitedBy) {
        this.invitedBy = invitedBy;
    }

    public String getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public LocalDateTime getQrExpiryTime() {
        return qrExpiryTime;
    }

    public void setQrExpiryTime(LocalDateTime qrExpiryTime) {
        this.qrExpiryTime = qrExpiryTime;
    }

    public Boolean getIsSingleUse() {
        return isSingleUse;
    }

    public void setIsSingleUse(Boolean isSingleUse) {
        this.isSingleUse = isSingleUse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    // Helper methods
    public boolean isInside() {
        return "ĐANG_TRONG_TÒA".equals(status);
    }

    public boolean isQrExpired() {
        if (qrExpiryTime == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(qrExpiryTime);
    }

    public boolean canEnter() {
        return "CHỜ_VÀO".equals(status) && !isQrExpired();
    }
}

