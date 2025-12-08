package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho loại phí dịch vụ
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 1. Quản lý loại phí (Fee Management)
 */
public class ServiceFeeType {
    private Integer id;
    private String feeCode;
    private String feeName;
    private String feeCategory; // QUẢN_LÝ, GỬI_XE, ĐIỆN, NƯỚC, VỆ_SINH, RÁC_THẢI, TIỆN_ÍCH_CHUNG, THANG_MÁY, KHÁC
    private String calculationMethod; // CỐ_ĐỊNH, THEO_DIỆN_TÍCH, THEO_SỐ_LƯỢNG, THEO_CÔNG_TƠ
    private Double unitPrice; // Đơn giá hiện hành
    private String unit; // VNĐ, VNĐ/m², VNĐ/người, VNĐ/kWh, VNĐ/m³
    private String billingCycle; // Chu kỳ thu: THÁNG, QUÝ, NĂM
    private Boolean autoGenerate; // Tự động sinh hóa đơn: có/không
    private String accountCode; // Tài khoản kế toán liên kết (nếu có)
    private Boolean isMandatory;
    private Boolean isActive;
    private String description;
    private LocalDate effectiveDate; // Ngày có hiệu lực của đơn giá hiện tại

    public ServiceFeeType() {
    }

    public ServiceFeeType(String feeCode, String feeName, String feeCategory, String calculationMethod) {
        this.feeCode = feeCode;
        this.feeName = feeName;
        this.feeCategory = feeCategory;
        this.calculationMethod = calculationMethod;
        this.isMandatory = true;
        this.isActive = true;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeCategory() {
        return feeCategory;
    }

    public void setFeeCategory(String feeCategory) {
        this.feeCategory = feeCategory;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Boolean getAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(Boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}


