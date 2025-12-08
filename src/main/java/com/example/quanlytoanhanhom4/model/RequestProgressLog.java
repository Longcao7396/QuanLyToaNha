package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho nhật ký tiến độ xử lý yêu cầu (Request Progress Tracking)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestProgressLog {
    private Integer id;
    private Integer ticketId; // ID của ticket/yêu cầu
    private String status; // Trạng thái mới
    private String previousStatus; // Trạng thái cũ
    private Integer updatedBy; // Người cập nhật (user_id)
    private String updateType; // Loại cập nhật: STATUS_CHANGE, ASSIGNMENT, NOTE, PROOF_UPLOAD, COST_UPDATE
    private String description; // Mô tả cập nhật
    private String proofImagePath; // Ảnh/video sau sửa chữa
    private Double costAmount; // Chi phí sửa chữa (nếu có)
    private String materialsUsed; // Vật tư đã sử dụng (JSON hoặc text)
    private LocalDateTime scheduledAppointment; // Hẹn lịch xử lý với cư dân (nếu cần vào nhà)
    private LocalDateTime actualCompletionTime; // Thời gian hoàn thành thực tế
    private String notes; // Ghi chú
    private LocalDateTime createdAt;

    public RequestProgressLog() {
    }

    public RequestProgressLog(Integer ticketId, String status, Integer updatedBy) {
        this.ticketId = ticketId;
        this.status = status;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProofImagePath() {
        return proofImagePath;
    }

    public void setProofImagePath(String proofImagePath) {
        this.proofImagePath = proofImagePath;
    }

    public Double getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(Double costAmount) {
        this.costAmount = costAmount;
    }

    public String getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(String materialsUsed) {
        this.materialsUsed = materialsUsed;
    }

    public LocalDateTime getScheduledAppointment() {
        return scheduledAppointment;
    }

    public void setScheduledAppointment(LocalDateTime scheduledAppointment) {
        this.scheduledAppointment = scheduledAppointment;
    }

    public LocalDateTime getActualCompletionTime() {
        return actualCompletionTime;
    }

    public void setActualCompletionTime(LocalDateTime actualCompletionTime) {
        this.actualCompletionTime = actualCompletionTime;
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
}

