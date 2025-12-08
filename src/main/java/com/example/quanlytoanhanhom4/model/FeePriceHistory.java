package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho lịch sử điều chỉnh giá phí
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 1.2. Quản lý đơn giá - Lịch sử điều chỉnh giá
 */
public class FeePriceHistory {
    private Integer id;
    private Integer feeTypeId;
    private Double oldPrice; // Giá cũ
    private Double newPrice; // Giá mới
    private LocalDate effectiveDate; // Ngày có hiệu lực
    private String reason; // Lý do điều chỉnh
    private Integer changedBy; // Người thay đổi (user_id)
    private String notes;
    private LocalDate createdAt;

    public FeePriceHistory() {
    }

    public FeePriceHistory(Integer feeTypeId, Double oldPrice, Double newPrice, LocalDate effectiveDate) {
        this.feeTypeId = feeTypeId;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.effectiveDate = effectiveDate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Integer changedBy) {
        this.changedBy = changedBy;
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

