package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

import java.util.List;

/**
 * Model cho công nợ
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 4. Thu tiền & công nợ - 4.2. Công nợ
 */
public class DebtRecord {
    private Integer id;
    private Integer apartmentId;
    private Integer residentId;
    private Double totalDebt; // Tổng công nợ đến hiện tại
    private Integer overdueMonths; // Số tháng quá hạn
    private LocalDateTime lastUpdated;
    private LocalDateTime lastReminderDate; // Ngày nhắc nợ gần nhất
    private Integer reminderCount; // Số lần đã nhắc nợ
    private String notes;
    
    // Danh sách hóa đơn còn nợ (không lưu trong DB, chỉ để hiển thị)
    private List<Invoice> unpaidInvoices;

    public DebtRecord() {
    }

    public DebtRecord(Integer apartmentId, Double totalDebt) {
        this.apartmentId = apartmentId;
        this.totalDebt = totalDebt;
        this.lastUpdated = LocalDateTime.now();
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

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(Double totalDebt) {
        this.totalDebt = totalDebt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getOverdueMonths() {
        return overdueMonths;
    }

    public void setOverdueMonths(Integer overdueMonths) {
        this.overdueMonths = overdueMonths;
    }

    public LocalDateTime getLastReminderDate() {
        return lastReminderDate;
    }

    public void setLastReminderDate(LocalDateTime lastReminderDate) {
        this.lastReminderDate = lastReminderDate;
    }

    public Integer getReminderCount() {
        return reminderCount;
    }

    public void setReminderCount(Integer reminderCount) {
        this.reminderCount = reminderCount;
    }

    public List<Invoice> getUnpaidInvoices() {
        return unpaidInvoices;
    }

    public void setUnpaidInvoices(List<Invoice> unpaidInvoices) {
        this.unpaidInvoices = unpaidInvoices;
    }
}


