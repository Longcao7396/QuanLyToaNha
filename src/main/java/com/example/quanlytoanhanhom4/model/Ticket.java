package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho ticket/yêu cầu & sự cố
 * Module 5: Quản lý yêu cầu & sự cố (Ticket System)
 */
public class Ticket {
    private Integer id;
    private String ticketNumber;
    private Integer apartmentId;
    private Integer residentId;
    private String ticketType; // SỬA_CHỮA, KHIẾU_NẠI, YÊU_CẦU, SỰ_CỐ, KHÁC
    private String category; // SỰ_CỐ_KỸ_THUẬT, PHÀN_NÀN_TIẾNG_ỒN, VỆ_SINH, HÀNG_XÓM_GÂY_RỐI, ĐỀ_NGHỊ_HỖ_TRỢ, ĐỀ_XUẤT_TIỆN_ÍCH, YÊU_CẦU_GIẤY_TỜ, KHÁC
    private String title;
    private String description;
    private String attachmentPath; // Hình ảnh/Video đính kèm
    private String location; // Khu vực gặp sự cố (nếu trong khu vực chung)
    private String priority; // THẤP, TRUNG_BÌNH, CAO, KHẨN_CẤP
    private String status; // TIẾP_NHẬN, ĐANG_XỬ_LÝ, CHỜ_CƯ_DÂN_PHẢN_HỒI, HOÀN_THÀNH, TỪ_CHỐI, ĐÓNG_YÊU_CẦU
    private String submissionChannel; // Kênh gửi: APP, LỄ_TÂN, WEB
    private String department; // Bộ phận xử lý: KỸ_THUẬT_ĐIỆN, KỸ_THUẬT_NƯỚC, BẢO_VỆ, VỆ_SINH, CSKH, BQL
    private Integer slaHours; // Thời hạn xử lý (SLA) - số giờ
    private LocalDateTime slaDeadline; // Thời hạn xử lý (SLA) - deadline
    private String rejectionReason; // Lý do từ chối (nếu có)
    private Boolean wantsReopen; // Cư dân muốn mở lại yêu cầu không
    private Integer receivedBy; // Người tiếp nhận
    private Integer assignedTo; // Kỹ thuật viên phụ trách
    private LocalDateTime receivedDate; // Thời gian tiếp nhận
    private LocalDateTime assignedDate;
    private LocalDateTime startTime; // Thời gian bắt đầu xử lý
    private LocalDateTime createdDate;
    private LocalDateTime resolvedDate;
    private LocalDateTime closedDate;
    private Double estimatedCost;
    private Double actualCost;
    private String resolution;
    private String internalNotes; // Ghi chú nội bộ/Chat
    private Integer satisfactionRating; // 1-5
    private String satisfactionFeedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket() {
    }

    public Ticket(Integer residentId, String ticketType, String category, String title, String description) {
        this.residentId = residentId;
        this.ticketType = ticketType;
        this.category = category;
        this.title = title;
        this.description = description;
        this.priority = "TRUNG_BÌNH";
        this.status = "TIẾP_NHẬN";
        this.submissionChannel = "APP";
        this.createdDate = LocalDateTime.now();
        this.receivedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public LocalDateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
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

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getSatisfactionRating() {
        return satisfactionRating;
    }

    public void setSatisfactionRating(Integer satisfactionRating) {
        this.satisfactionRating = satisfactionRating;
    }

    public String getSatisfactionFeedback() {
        return satisfactionFeedback;
    }

    public void setSatisfactionFeedback(String satisfactionFeedback) {
        this.satisfactionFeedback = satisfactionFeedback;
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

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    // Helper methods
    public String getSubmissionChannel() {
        return submissionChannel;
    }

    public void setSubmissionChannel(String submissionChannel) {
        this.submissionChannel = submissionChannel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(Integer slaHours) {
        this.slaHours = slaHours;
    }

    public LocalDateTime getSlaDeadline() {
        return slaDeadline;
    }

    public void setSlaDeadline(LocalDateTime slaDeadline) {
        this.slaDeadline = slaDeadline;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Boolean getWantsReopen() {
        return wantsReopen;
    }

    public void setWantsReopen(Boolean wantsReopen) {
        this.wantsReopen = wantsReopen;
    }

    // Helper methods
    public boolean isResolved() {
        return "HOÀN_THÀNH".equals(status);
    }

    public boolean isWaitingForFeedback() {
        return "CHỜ_CƯ_DÂN_PHẢN_HỒI".equals(status);
    }

    public boolean isRejected() {
        return "TỪ_CHỐI".equals(status);
    }

    public boolean isClosed() {
        return "ĐÓNG_YÊU_CẦU".equals(status);
    }

    public boolean isSlaOverdue() {
        if (slaDeadline == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(slaDeadline) && !isResolved() && !isClosed();
    }

    public boolean isOverdue() {
        if (assignedDate == null || priority == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        long hoursSinceAssigned = java.time.Duration.between(assignedDate, now).toHours();
        
        switch (priority) {
            case "KHẨN":
                return hoursSinceAssigned > 4;
            case "CAO":
                return hoursSinceAssigned > 24;
            case "TRUNG_BÌNH":
                return hoursSinceAssigned > 72;
            case "THẤP":
                return hoursSinceAssigned > 168; // 7 days
            default:
                return false;
        }
    }
}


