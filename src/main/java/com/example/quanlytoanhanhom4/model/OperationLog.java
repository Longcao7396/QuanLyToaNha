package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model cho nhật ký vận hành (Operation Log)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class OperationLog {
    private Integer id;
    private LocalDate logDate; // Ngày ghi nhận
    private String shift; // Ca trực: SÁNG, CHIỀU, ĐÊM
    private Integer loggedBy; // Người ghi nhận (user_id)
    private Double totalElectricityReading; // Chỉ số điện tổng
    private Double waterPressure; // Áp suất nước
    private String elevatorStatus; // Tình trạng thang máy
    private String fireSafetyCheck; // Kiểm tra hệ thống PCCC
    private String abnormalities; // Ghi chú các bất thường trong ca trực
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OperationLog() {
    }

    public OperationLog(LocalDate logDate, String shift, Integer loggedBy) {
        this.logDate = logDate;
        this.shift = shift;
        this.loggedBy = loggedBy;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Integer getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(Integer loggedBy) {
        this.loggedBy = loggedBy;
    }

    public Double getTotalElectricityReading() {
        return totalElectricityReading;
    }

    public void setTotalElectricityReading(Double totalElectricityReading) {
        this.totalElectricityReading = totalElectricityReading;
    }

    public Double getWaterPressure() {
        return waterPressure;
    }

    public void setWaterPressure(Double waterPressure) {
        this.waterPressure = waterPressure;
    }

    public String getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(String elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public String getFireSafetyCheck() {
        return fireSafetyCheck;
    }

    public void setFireSafetyCheck(String fireSafetyCheck) {
        this.fireSafetyCheck = fireSafetyCheck;
    }

    public String getAbnormalities() {
        return abnormalities;
    }

    public void setAbnormalities(String abnormalities) {
        this.abnormalities = abnormalities;
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
}

