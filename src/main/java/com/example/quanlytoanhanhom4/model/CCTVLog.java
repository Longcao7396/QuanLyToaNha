package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho camera giám sát – lưu trữ (CCTV Management)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class CCTVLog {
    private Integer id;
    private String cameraCode; // Mã camera
    private String cameraLocation; // Vị trí: TẦNG_HẦM, SẢNH, THANG_MÁY, HÀNH_LANG
    private String cameraName; // Tên camera
    private LocalDateTime recordTime; // Thời gian ghi
    private String eventType; // Loại sự kiện: BÌNH_THƯỜNG, CHUYỂN_ĐỘNG, BẤT_THƯỜNG, CẢNH_BÁO
    private String videoPath; // Đường dẫn video
    private String imagePath; // Đường dẫn ảnh (motion detection)
    private Boolean isMotionDetected; // Phát hiện chuyển động
    private String description; // Mô tả sự kiện
    private Integer viewedBy; // Người đã xem (user_id)
    private LocalDateTime viewedAt; // Thời gian xem
    private Boolean isExtracted; // Đã trích xuất video
    private LocalDateTime extractedAt; // Thời gian trích xuất
    private String extractedBy; // Người trích xuất
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CCTVLog() {
    }

    public CCTVLog(String cameraCode, String cameraLocation, LocalDateTime recordTime) {
        this.cameraCode = cameraCode;
        this.cameraLocation = cameraLocation;
        this.recordTime = recordTime;
        this.eventType = "BÌNH_THƯỜNG";
        this.isMotionDetected = false;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCameraCode() {
        return cameraCode;
    }

    public void setCameraCode(String cameraCode) {
        this.cameraCode = cameraCode;
    }

    public String getCameraLocation() {
        return cameraLocation;
    }

    public void setCameraLocation(String cameraLocation) {
        this.cameraLocation = cameraLocation;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getIsMotionDetected() {
        return isMotionDetected;
    }

    public void setIsMotionDetected(Boolean isMotionDetected) {
        this.isMotionDetected = isMotionDetected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getViewedBy() {
        return viewedBy;
    }

    public void setViewedBy(Integer viewedBy) {
        this.viewedBy = viewedBy;
    }

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }

    public Boolean getIsExtracted() {
        return isExtracted;
    }

    public void setIsExtracted(Boolean isExtracted) {
        this.isExtracted = isExtracted;
    }

    public LocalDateTime getExtractedAt() {
        return extractedAt;
    }

    public void setExtractedAt(LocalDateTime extractedAt) {
        this.extractedAt = extractedAt;
    }

    public String getExtractedBy() {
        return extractedBy;
    }

    public void setExtractedBy(String extractedBy) {
        this.extractedBy = extractedBy;
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
    public boolean isAbnormal() {
        return "BẤT_THƯỜNG".equals(eventType) || "CẢNH_BÁO".equals(eventType);
    }

    public boolean hasMotion() {
        return isMotionDetected != null && isMotionDetected;
    }
}

