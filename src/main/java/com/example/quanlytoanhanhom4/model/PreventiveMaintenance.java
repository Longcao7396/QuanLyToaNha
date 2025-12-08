package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model cho bảo trì định kỳ (Preventive Maintenance)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class PreventiveMaintenance {
    private Integer id;
    private String maintenanceCode; // Mã bảo trì
    private Integer assetId; // Thiết bị
    private String maintenanceType; // Loại bảo trì: ĐỊNH_KỲ, ĐỘT_XUẤT
    private String scheduleType; // Chu kỳ: TUẦN, THÁNG, QUÝ, NĂM
    private Integer scheduleInterval; // Số tuần/tháng/quý/năm
    private LocalDate scheduledDate; // Ngày dự kiến
    private LocalDate actualDate; // Ngày thực hiện
    private Integer assignedTo; // Người phụ trách (user_id)
    private String workItems; // Hạng mục công việc (checklist) - JSON hoặc text
    private String checklist; // Checklist công việc cần làm
    private String materialsUsed; // Vật tư cần dùng (JSON hoặc text)
    private Double estimatedCost; // Chi phí dự kiến
    private Double actualCost; // Chi phí thực tế
    private String conditionAfter; // Tình trạng sau bảo trì
    private String beforeImagePath; // Hình ảnh trước khi bảo trì
    private String afterImagePath; // Hình ảnh sau khi bảo trì
    private String confirmationImagePath; // Hình ảnh xác nhận
    private String contractor; // Nhà thầu bảo trì (nếu thuê ngoài)
    private String technicalDocumentPath; // Tài liệu/hướng dẫn kỹ thuật
    private Integer notificationDaysBefore; // Gửi thông báo trước X ngày
    private String notes;
    private String status; // CHƯA_THỰC_HIỆN, ĐANG_THỰC_HIỆN, HOÀN_THÀNH, BỎ_QUA, TRỄ_HẠN
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PreventiveMaintenance() {
    }

    public PreventiveMaintenance(Integer assetId, String maintenanceType, String scheduleType, LocalDate scheduledDate) {
        this.assetId = assetId;
        this.maintenanceType = maintenanceType;
        this.scheduleType = scheduleType;
        this.scheduledDate = scheduledDate;
        this.status = "CHƯA_THỰC_HIỆN";
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaintenanceCode() {
        return maintenanceCode;
    }

    public void setMaintenanceCode(String maintenanceCode) {
        this.maintenanceCode = maintenanceCode;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Integer getScheduleInterval() {
        return scheduleInterval;
    }

    public void setScheduleInterval(Integer scheduleInterval) {
        this.scheduleInterval = scheduleInterval;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getActualDate() {
        return actualDate;
    }

    public void setActualDate(LocalDate actualDate) {
        this.actualDate = actualDate;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getWorkItems() {
        return workItems;
    }

    public void setWorkItems(String workItems) {
        this.workItems = workItems;
    }

    public String getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(String materialsUsed) {
        this.materialsUsed = materialsUsed;
    }

    public Double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public String getConditionAfter() {
        return conditionAfter;
    }

    public void setConditionAfter(String conditionAfter) {
        this.conditionAfter = conditionAfter;
    }

    public String getConfirmationImagePath() {
        return confirmationImagePath;
    }

    public void setConfirmationImagePath(String confirmationImagePath) {
        this.confirmationImagePath = confirmationImagePath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    public boolean isCompleted() {
        return "HOÀN_THÀNH".equals(status);
    }

    public boolean isOverdue() {
        if (scheduledDate == null || "HOÀN_THÀNH".equals(status) || "BỎ_QUA".equals(status)) {
            return false;
        }
        return LocalDate.now().isAfter(scheduledDate);
    }

    // Getters and Setters cho các trường mới
    public String getChecklist() {
        return checklist;
    }

    public void setChecklist(String checklist) {
        this.checklist = checklist;
    }

    public String getBeforeImagePath() {
        return beforeImagePath;
    }

    public void setBeforeImagePath(String beforeImagePath) {
        this.beforeImagePath = beforeImagePath;
    }

    public String getAfterImagePath() {
        return afterImagePath;
    }

    public void setAfterImagePath(String afterImagePath) {
        this.afterImagePath = afterImagePath;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getTechnicalDocumentPath() {
        return technicalDocumentPath;
    }

    public void setTechnicalDocumentPath(String technicalDocumentPath) {
        this.technicalDocumentPath = technicalDocumentPath;
    }

    public Integer getNotificationDaysBefore() {
        return notificationDaysBefore;
    }

    public void setNotificationDaysBefore(Integer notificationDaysBefore) {
        this.notificationDaysBefore = notificationDaysBefore;
    }
}

