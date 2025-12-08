package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho hợp đồng thuê
 * Tính năng thực tiễn: Quản lý hợp đồng thuê, hạn hợp đồng, cảnh báo sắp hết hạn
 */
public class RentalContract {
    private Integer id;
    private Integer apartmentId;
    private Integer ownerId; // Chủ hộ (owner)
    private Integer residentId; // Người thuê (tenant)
    private String contractNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double monthlyRent; // Giá thuê
    private Double deposit; // Tiền đặt cọc
    private String contractFilePath; // File hợp đồng PDF
    private String specialTerms; // Các điều khoản đặc biệt
    private String renewalHistory; // Lịch sử gia hạn (JSON hoặc text)
    private String status; // ACTIVE, EXPIRED, TERMINATED
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public RentalContract() {
    }

    public RentalContract(Integer apartmentId, Integer residentId, LocalDate startDate, LocalDate endDate) {
        this.apartmentId = apartmentId;
        this.residentId = residentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "ACTIVE";
    }

    /**
     * Kiểm tra hợp đồng có sắp hết hạn không (30 ngày)
     */
    public boolean isExpiringSoon() {
        if (endDate == null || !status.equals("ACTIVE")) {
            return false;
        }
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        return endDate.isBefore(thirtyDaysFromNow) || endDate.isEqual(thirtyDaysFromNow);
    }

    /**
     * Kiểm tra hợp đồng đã hết hạn chưa
     */
    public boolean isExpired() {
        if (endDate == null) {
            return false;
        }
        return endDate.isBefore(LocalDate.now()) && status.equals("ACTIVE");
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

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getContractFilePath() {
        return contractFilePath;
    }

    public void setContractFilePath(String contractFilePath) {
        this.contractFilePath = contractFilePath;
    }

    public String getSpecialTerms() {
        return specialTerms;
    }

    public void setSpecialTerms(String specialTerms) {
        this.specialTerms = specialTerms;
    }

    public String getRenewalHistory() {
        return renewalHistory;
    }

    public void setRenewalHistory(String renewalHistory) {
        this.renewalHistory = renewalHistory;
    }
}


