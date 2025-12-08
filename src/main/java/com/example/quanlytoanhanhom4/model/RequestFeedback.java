package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho phản hồi và đánh giá của cư dân (Resident Feedback)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestFeedback {
    private Integer id;
    private Integer ticketId; // ID của ticket/yêu cầu
    private Integer residentId; // Cư dân đánh giá
    private Integer satisfactionRating; // Điểm hài lòng: 1-5 sao
    private String detailedComment; // Nhận xét chi tiết
    private String feedbackImagePath; // Ảnh/video cư dân gửi (optional)
    private Boolean wantsReopen; // Có muốn mở lại yêu cầu không
    private String reopenReason; // Lý do muốn mở lại
    private LocalDateTime feedbackDate; // Thời gian phản hồi
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RequestFeedback() {
    }

    public RequestFeedback(Integer ticketId, Integer residentId, Integer satisfactionRating) {
        this.ticketId = ticketId;
        this.residentId = residentId;
        this.satisfactionRating = satisfactionRating;
        this.feedbackDate = LocalDateTime.now();
        this.wantsReopen = false;
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

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Integer getSatisfactionRating() {
        return satisfactionRating;
    }

    public void setSatisfactionRating(Integer satisfactionRating) {
        this.satisfactionRating = satisfactionRating;
    }

    public String getDetailedComment() {
        return detailedComment;
    }

    public void setDetailedComment(String detailedComment) {
        this.detailedComment = detailedComment;
    }

    public String getFeedbackImagePath() {
        return feedbackImagePath;
    }

    public void setFeedbackImagePath(String feedbackImagePath) {
        this.feedbackImagePath = feedbackImagePath;
    }

    public Boolean getWantsReopen() {
        return wantsReopen;
    }

    public void setWantsReopen(Boolean wantsReopen) {
        this.wantsReopen = wantsReopen;
    }

    public String getReopenReason() {
        return reopenReason;
    }

    public void setReopenReason(String reopenReason) {
        this.reopenReason = reopenReason;
    }

    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
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
    public boolean isSatisfied() {
        return satisfactionRating != null && satisfactionRating >= 4;
    }

    public boolean isUnsatisfied() {
        return satisfactionRating != null && satisfactionRating <= 2;
    }
}

