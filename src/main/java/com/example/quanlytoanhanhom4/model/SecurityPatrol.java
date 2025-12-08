package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho tuần tra – chấm điểm an ninh (Security Patrol)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class SecurityPatrol {
    private Integer id;
    private String patrolCode; // Mã tuần tra
    private String checkpointCode; // Mã điểm tuần tra (QR/NFC)
    private String checkpointLocation; // Vị trí điểm tuần tra
    private Integer patrolledBy; // Người phụ trách (user_id)
    private LocalDateTime scheduledTime; // Giờ cần đi qua
    private LocalDateTime actualTime; // Giờ thực tế đi qua
    private String proofImagePath; // Bằng chứng: ảnh
    private String notes; // Ghi chú
    private String status; // Trạng thái: CHƯA_KIỂM_TRA, ĐÃ_KIỂM_TRA, BỎ_SÓT, TRỄ
    private String shift; // Ca trực: SÁNG, CHIỀU, ĐÊM
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SecurityPatrol() {
    }

    public SecurityPatrol(String checkpointCode, String checkpointLocation, Integer patrolledBy, LocalDateTime scheduledTime) {
        this.checkpointCode = checkpointCode;
        this.checkpointLocation = checkpointLocation;
        this.patrolledBy = patrolledBy;
        this.scheduledTime = scheduledTime;
        this.status = "CHƯA_KIỂM_TRA";
        this.actualTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatrolCode() {
        return patrolCode;
    }

    public void setPatrolCode(String patrolCode) {
        this.patrolCode = patrolCode;
    }

    public String getCheckpointCode() {
        return checkpointCode;
    }

    public void setCheckpointCode(String checkpointCode) {
        this.checkpointCode = checkpointCode;
    }

    public String getCheckpointLocation() {
        return checkpointLocation;
    }

    public void setCheckpointLocation(String checkpointLocation) {
        this.checkpointLocation = checkpointLocation;
    }

    public Integer getPatrolledBy() {
        return patrolledBy;
    }

    public void setPatrolledBy(Integer patrolledBy) {
        this.patrolledBy = patrolledBy;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getActualTime() {
        return actualTime;
    }

    public void setActualTime(LocalDateTime actualTime) {
        this.actualTime = actualTime;
    }

    public String getProofImagePath() {
        return proofImagePath;
    }

    public void setProofImagePath(String proofImagePath) {
        this.proofImagePath = proofImagePath;
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

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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
    public boolean isChecked() {
        return "ĐÃ_KIỂM_TRA".equals(status);
    }

    public boolean isMissed() {
        return "BỎ_SÓT".equals(status);
    }

    public boolean isLate() {
        if (scheduledTime == null || actualTime == null) {
            return false;
        }
        return actualTime.isAfter(scheduledTime.plusMinutes(15)); // Trễ hơn 15 phút
    }

    public long getDelayMinutes() {
        if (scheduledTime == null || actualTime == null) {
            return 0;
        }
        if (actualTime.isBefore(scheduledTime)) {
            return 0;
        }
        return java.time.Duration.between(scheduledTime, actualTime).toMinutes();
    }
}

