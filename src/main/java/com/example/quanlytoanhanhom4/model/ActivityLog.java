package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho nhật ký & lịch sử thay đổi
 * Module 1: Quản lý cư dân & căn hộ
 * 6. Nhật ký & lịch sử thay đổi
 * Đảm bảo minh bạch cho BQL
 */
public class ActivityLog {
    private Integer id;
    private String entityType; // Loại entity: APARTMENT, RESIDENT, OWNER, RENTAL_CONTRACT, ACCESS_CARD
    private Integer entityId; // ID của entity
    private String action; // Hành động: CREATE, UPDATE, DELETE, MOVE_IN, MOVE_OUT, TRANSFER, CARD_ISSUED, CARD_LOCKED, CONTRACT_CREATED, CONTRACT_RENEWED, CONTRACT_ENDED
    private Integer userId; // Người thực hiện (user_id của admin/BQL)
    private String userName; // Tên người thực hiện (để hiển thị nhanh)
    private String oldValue; // Giá trị cũ (JSON hoặc text)
    private String newValue; // Giá trị mới (JSON hoặc text)
    private String description; // Mô tả chi tiết
    private LocalDateTime actionTime; // Thời gian thực hiện
    private String ipAddress; // Địa chỉ IP (nếu cần)
    private String notes;

    public ActivityLog() {
        this.actionTime = LocalDateTime.now();
    }

    public ActivityLog(String entityType, Integer entityId, String action, Integer userId) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.userId = userId;
        this.actionTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

