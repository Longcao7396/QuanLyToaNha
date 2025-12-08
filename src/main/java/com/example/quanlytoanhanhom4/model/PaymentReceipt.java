package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho phiếu thu
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 4. Thu tiền & công nợ - 4.3. Phiếu thu
 */
public class PaymentReceipt {
    private Integer id;
    private String receiptNumber; // Số phiếu thu
    private Integer invoiceId; // Hóa đơn được thanh toán
    private Integer apartmentId;
    private Integer residentId;
    private Double paymentAmount; // Số tiền thu
    private String paymentMethod; // Tiền mặt, Chuyển khoản, Cổng thanh toán, QR banking
    private String paymentReference; // Số tham chiếu giao dịch
    private LocalDateTime paymentDate; // Ngày thanh toán
    private Integer collectedBy; // Nhân viên thu (user_id)
    private String receiptImagePath; // Đính kèm ảnh biên lai (nếu có)
    private String notes;
    private LocalDateTime createdAt;

    public PaymentReceipt() {
        this.paymentDate = LocalDateTime.now();
    }

    public PaymentReceipt(Integer invoiceId, Double paymentAmount, String paymentMethod) {
        this.invoiceId = invoiceId;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
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

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(Integer collectedBy) {
        this.collectedBy = collectedBy;
    }

    public String getReceiptImagePath() {
        return receiptImagePath;
    }

    public void setReceiptImagePath(String receiptImagePath) {
        this.receiptImagePath = receiptImagePath;
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
}

