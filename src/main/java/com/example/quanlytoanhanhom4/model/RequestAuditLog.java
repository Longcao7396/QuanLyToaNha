package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho nhật ký kiểm soát (Audit Trail)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestAuditLog {
    private Integer id;
    private Integer ticketId; // ID của ticket/yêu cầu
    private String action; // Hành động: CREATE, UPDATE, STATUS_CHANGE, ASSIGNMENT_CHANGE, DELETE, VIEW
    private Integer userId; // Người thực hiện (user_id)
    private String userName; // Tên người thực hiện (để lưu lại)
    private String fieldName; // Tên trường thay đổi (nếu là UPDATE)
    private String oldValue; // Giá trị cũ
    private String newValue; // Giá trị mới
    private String description; // Mô tả chi tiết
    private String ipAddress; // Địa chỉ IP
    private LocalDateTime actionTime; // Thời gian thực hiện
    private String notes;

    public RequestAuditLog() {
    }

    public RequestAuditLog(Integer ticketId, String action, Integer userId) {
        this.ticketId = ticketId;
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

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
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

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

