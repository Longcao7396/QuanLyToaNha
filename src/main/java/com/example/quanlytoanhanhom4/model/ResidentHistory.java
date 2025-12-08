package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho lịch sử cư dân
 * Tính năng thực tiễn: Theo dõi lịch sử cư dân (ai đã ở, ai đã chuyển đi)
 */
public class ResidentHistory {
    private Integer id;
    private Integer residentId;
    private Integer apartmentId;
    private String action; // MOVED_IN, MOVED_OUT, UPDATED
    private LocalDate actionDate;
    private String notes;
    private LocalDate createdAt;

    public ResidentHistory() {
    }

    public ResidentHistory(Integer residentId, Integer apartmentId, String action) {
        this.residentId = residentId;
        this.apartmentId = apartmentId;
        this.action = action;
        this.actionDate = LocalDate.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}


