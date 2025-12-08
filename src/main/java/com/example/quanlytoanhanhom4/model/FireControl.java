package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho tích hợp PCCC (Fire Control)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class FireControl {
    private Integer id;
    private String alarmCode; // Mã báo cháy
    private String location; // Vị trí báo cháy
    private String buildingBlock; // Block/Tòa nhà
    private String floor; // Tầng
    private String zone; // Khu vực
    private LocalDateTime alarmTime; // Thời gian báo cháy
    private String alarmType; // Loại: BÁO_CHÁY, SỰ_CỐ, KIỂM_TRA, BÁO_GIẢ
    private String severity; // Mức độ: THẤP, TRUNG_BÌNH, CAO, KHẨN_CẤP
    private Boolean isConfirmed; // Đã xác nhận
    private LocalDateTime confirmedAt; // Thời gian xác nhận
    private Integer confirmedBy; // Người xác nhận (user_id)
    private Boolean isResolved; // Đã xử lý
    private LocalDateTime resolvedAt; // Thời gian xử lý
    private String resolution; // Cách xử lý
    private Boolean notificationSent; // Đã gửi cảnh báo đến cư dân
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FireControl() {
    }

    public FireControl(String location, String alarmType, LocalDateTime alarmTime) {
        this.location = location;
        this.alarmType = alarmType;
        this.alarmTime = alarmTime;
        this.severity = "TRUNG_BÌNH";
        this.isConfirmed = false;
        this.isResolved = false;
        this.notificationSent = false;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlarmCode() {
        return alarmCode;
    }

    public void setAlarmCode(String alarmCode) {
        this.alarmCode = alarmCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuildingBlock() {
        return buildingBlock;
    }

    public void setBuildingBlock(String buildingBlock) {
        this.buildingBlock = buildingBlock;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public Integer getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(Integer confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Boolean getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(Boolean notificationSent) {
        this.notificationSent = notificationSent;
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
    public boolean isUrgent() {
        return "KHẨN_CẤP".equals(severity) || "CAO".equals(severity);
    }

    public boolean isFalseAlarm() {
        return "BÁO_GIẢ".equals(alarmType);
    }
}

