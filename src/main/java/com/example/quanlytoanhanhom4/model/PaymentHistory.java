package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho lịch sử thanh toán
 * Module 2: Quản lý phí & công nợ
 */
public class PaymentHistory {
    private Integer id;
    private Integer invoiceId;
    private Double paymentAmount;
    private String paymentMethod; // CASH, BANK_TRANSFER, VNPAY, MOMO
    private String paymentReference;
    private LocalDateTime paymentDate;
    private Integer processedBy;
    private String notes;

    public PaymentHistory() {
    }

    public PaymentHistory(Integer invoiceId, Double paymentAmount, String paymentMethod) {
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

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
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

    public Integer getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedBy = processedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}













