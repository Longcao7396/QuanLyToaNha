package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho phí dịch vụ theo căn hộ
 * Module 3: Quản lý phí dịch vụ
 */
public class ApartmentServiceFee {
    private Integer id;
    private Integer apartmentId;
    private Integer feeTypeId;
    private Integer periodMonth;
    private Integer periodYear;
    private Double previousReading;
    private Double currentReading;
    private Double consumption;
    private Double unitPrice;
    private Double totalAmount;
    private LocalDate dueDate;
    private String status; // PENDING, PAID, OVERDUE, CANCELLED
    private LocalDate paidDate;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public ApartmentServiceFee() {
    }

    public ApartmentServiceFee(Integer apartmentId, Integer feeTypeId, Integer periodMonth, Integer periodYear) {
        this.apartmentId = apartmentId;
        this.feeTypeId = feeTypeId;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.status = "PENDING";
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

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
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

    public Double getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(Double previousReading) {
        this.previousReading = previousReading;
    }

    public Double getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(Double currentReading) {
        this.currentReading = currentReading;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
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

    // Helper method to calculate consumption
    public void calculateConsumption() {
        if (previousReading != null && currentReading != null) {
            this.consumption = currentReading - previousReading;
        }
    }

    // Helper method to calculate total amount
    public void calculateTotalAmount() {
        if (consumption != null && unitPrice != null) {
            this.totalAmount = consumption * unitPrice;
        } else if (unitPrice != null) {
            this.totalAmount = unitPrice; // For fixed fees
        }
    }
}













