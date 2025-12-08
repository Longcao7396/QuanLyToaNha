package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho công tơ điện nước
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 5. Điện – Nước – Gas (Utility Billing)
 */
public class UtilityMeter {
    private Integer id;
    private Integer apartmentId;
    private String meterType; // Loại công tơ: ĐIỆN, NƯỚC, GAS
    private String meterCode; // Mã công tơ
    private Double currentReading; // Chỉ số hiện tại
    private Double previousReading; // Chỉ số kỳ trước
    private LocalDate readingDate; // Ngày đọc chỉ số
    private Integer periodMonth; // Tháng
    private Integer periodYear; // Năm
    private Double consumption; // Lượng tiêu thụ (tự động tính)
    private Double unitPrice; // Đơn giá
    private Double totalAmount; // Thành tiền
    private Boolean isAbnormal; // Phát hiện bất thường (sử dụng tăng đột biến)
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public UtilityMeter() {
    }

    public UtilityMeter(Integer apartmentId, String meterType, String meterCode) {
        this.apartmentId = apartmentId;
        this.meterType = meterType;
        this.meterCode = meterCode;
    }

    /**
     * Tính lượng tiêu thụ
     */
    public void calculateConsumption() {
        if (previousReading != null && currentReading != null) {
            this.consumption = currentReading - previousReading;
            // Phát hiện bất thường: tăng > 200% so với kỳ trước
            // (Logic này có thể được cải thiện thêm)
            if (this.consumption < 0) {
                this.isAbnormal = true;
            }
        }
    }

    /**
     * Tính thành tiền
     */
    public void calculateAmount() {
        if (consumption != null && unitPrice != null) {
            this.totalAmount = consumption * unitPrice;
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public Double getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(Double currentReading) {
        this.currentReading = currentReading;
        this.calculateConsumption();
        this.calculateAmount();
    }

    public Double getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(Double previousReading) {
        this.previousReading = previousReading;
        this.calculateConsumption();
        this.calculateAmount();
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDate readingDate) {
        this.readingDate = readingDate;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
        this.calculateAmount();
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        this.calculateAmount();
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Boolean isAbnormal) {
        this.isAbnormal = isAbnormal;
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

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}

