package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho bảo trì đột xuất (Corrective Maintenance)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class CorrectiveMaintenance {
    private Integer id;
    private String maintenanceCode; // Mã bảo trì đột xuất
    private Integer assetId; // Thiết bị gặp sự cố
    private Integer ticketId; // Liên kết với ticket/yêu cầu (nếu có)
    private String issueDescription; // Mô tả hiện trạng
    private String urgency; // Mức độ khẩn cấp: THẤP, TRUNG_BÌNH, CAO, KHẨN_CẤP
    private Integer reportedBy; // Người phát hiện (user_id)
    private String reportedByName; // Tên người phát hiện
    private String issueImagePath; // Ảnh/video hiện trường
    private Integer assignedTo; // Nhân viên xử lý (user_id)
    private LocalDateTime reportedDate; // Thời gian phát hiện
    private LocalDateTime assignedDate; // Thời gian phân công
    private LocalDateTime startTime; // Thời gian bắt đầu xử lý
    private LocalDateTime completedTime; // Thời gian xử lý thực tế
    private String materialsUsed; // Vật tư sử dụng (JSON hoặc text)
    private Double materialCost; // Chi phí vật tư
    private Double laborCost; // Chi phí nhân công
    private Double totalCost; // Tổng chi phí phát sinh
    private String rootCause; // Nguyên nhân lỗi
    private String solution; // Biện pháp xử lý
    private String preventiveMeasures; // Biện pháp phòng ngừa tái diễn
    private String status; // MỚI_TẠO, ĐANG_XỬ_LÝ, HOÀN_THÀNH, HỦY
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CorrectiveMaintenance() {
    }

    public CorrectiveMaintenance(Integer assetId, String issueDescription, Integer reportedBy) {
        this.assetId = assetId;
        this.issueDescription = issueDescription;
        this.reportedBy = reportedBy;
        this.urgency = "TRUNG_BÌNH";
        this.status = "MỚI_TẠO";
        this.reportedDate = LocalDateTime.now();
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

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public Integer getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Integer reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public String getIssueImagePath() {
        return issueImagePath;
    }

    public void setIssueImagePath(String issueImagePath) {
        this.issueImagePath = issueImagePath;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public String getMaterialsUsed() {
        return materialsUsed;
    }

    public void setMaterialsUsed(String materialsUsed) {
        this.materialsUsed = materialsUsed;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
    }

    public Double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(Double laborCost) {
        this.laborCost = laborCost;
    }

    public Double getTotalCost() {
        if (totalCost != null) {
            return totalCost;
        }
        // Tự động tính nếu chưa set
        double cost = 0.0;
        if (materialCost != null) cost += materialCost;
        if (laborCost != null) cost += laborCost;
        return cost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getPreventiveMeasures() {
        return preventiveMeasures;
    }

    public void setPreventiveMeasures(String preventiveMeasures) {
        this.preventiveMeasures = preventiveMeasures;
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
    public boolean isCompleted() {
        return "HOÀN_THÀNH".equals(status);
    }

    public long getProcessingTimeHours() {
        if (startTime == null || completedTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, completedTime).toHours();
    }

    public boolean isOverdue(int maxHours) {
        if (startTime == null || isCompleted()) {
            return false;
        }
        long hours = java.time.Duration.between(startTime, LocalDateTime.now()).toHours();
        return hours > maxHours;
    }
}

