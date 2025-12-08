package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

/**
 * Model cho hóa đơn
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 3. Hóa đơn
 */
public class Invoice {
    private Integer id;
    private String invoiceNumber;
    private Integer apartmentId;
    private Integer residentId;
    private Integer periodMonth; // Tháng áp dụng
    private Integer periodYear; // Năm áp dụng
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;
    private String status; // CHƯA_THANH_TOÁN, ĐÃ_THANH_TOÁN, THANH_TOÁN_MỘT_PHẦN, QUÁ_HẠN, ĐÃ_HỦY
    private Boolean isLocked; // Khóa hóa đơn sau khi chốt sổ
    private Boolean isSent; // Đã gửi hóa đơn cho cư dân
    private LocalDate sentDate; // Ngày gửi hóa đơn
    private String paymentMethod; // CASH, BANK_TRANSFER, VNPAY, MOMO, QR_BANKING, OTHER
    private String paymentReference;
    private LocalDate paidDate;
    private String pdfPath;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Danh sách chi tiết hóa đơn (không lưu trong DB, chỉ để hiển thị)
    private List<InvoiceItem> items;

    public Invoice() {
    }

    public Invoice(Integer apartmentId, String invoiceNumber, LocalDate invoiceDate, LocalDate dueDate, Double totalAmount) {
        this.apartmentId = apartmentId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.paidAmount = 0.0;
        this.remainingAmount = totalAmount;
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

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Helper methods
    public boolean isOverdue() {
        if (dueDate == null || "PAID".equals(status) || "CANCELLED".equals(status)) {
            return false;
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public void updateStatus() {
        if (remainingAmount == null || totalAmount == null) {
            return;
        }
        if (remainingAmount <= 0) {
            this.status = "ĐÃ_THANH_TOÁN";
        } else if (paidAmount > 0 && paidAmount < totalAmount) {
            this.status = "THANH_TOÁN_MỘT_PHẦN";
        } else if (isOverdue()) {
            this.status = "QUÁ_HẠN";
        } else {
            this.status = "CHƯA_THANH_TOÁN";
        }
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

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public LocalDate getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
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


