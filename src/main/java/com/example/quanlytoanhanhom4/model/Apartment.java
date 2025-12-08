package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho căn hộ
 * Module 1: Quản lý cư dân & căn hộ
 * 1.1. Cấu trúc tòa nhà → block → tầng → căn hộ
 * 1.2. Thông tin mỗi căn hộ
 */
public class Apartment {
    private Integer id;
    private String apartmentCode; // Mã căn hộ (A-12.08)
    private String apartmentNo; // Giữ lại để tương thích
    private String buildingBlock; // Block (A, B, C)
    private Integer floorNumber; // Số tầng
    private Double area; // Diện tích (m²)
    private String apartmentType; // Loại căn (1PN/2PN/3PN)
    private Integer numberOfRooms; // Giữ lại để tương thích
    private String status; // ĐANG_Ở, ĐỂ_TRỐNG, CHO_THUÊ, SỬA_CHỮA
    private Integer ownerId; // ID của chủ hộ
    private Integer maxOccupants; // Số người tối đa
    private String internalNotes; // Ghi chú nội bộ
    private String notes; // Giữ lại để tương thích
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Apartment() {
    }

    public Apartment(String apartmentCode, String buildingBlock, Integer floorNumber, Double area, String apartmentType) {
        this.apartmentCode = apartmentCode;
        this.apartmentNo = apartmentCode; // Tương thích với code cũ
        this.buildingBlock = buildingBlock;
        this.floorNumber = floorNumber;
        this.area = area;
        this.apartmentType = apartmentType;
        this.status = "ĐỂ_TRỐNG";
    }

    public Apartment(String apartmentNo, String buildingBlock, Integer floorNumber, Double area, Integer numberOfRooms) {
        this.apartmentNo = apartmentNo;
        this.apartmentCode = apartmentNo; // Tương thích
        this.buildingBlock = buildingBlock;
        this.floorNumber = floorNumber;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.status = "ĐỂ_TRỐNG";
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getBuildingBlock() {
        return buildingBlock;
    }

    public void setBuildingBlock(String buildingBlock) {
        this.buildingBlock = buildingBlock;
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

    public String getApartmentCode() {
        return apartmentCode != null ? apartmentCode : apartmentNo;
    }

    public void setApartmentCode(String apartmentCode) {
        this.apartmentCode = apartmentCode;
        if (this.apartmentNo == null) {
            this.apartmentNo = apartmentCode;
        }
    }

    public String getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(String apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Integer getMaxOccupants() {
        return maxOccupants;
    }

    public void setMaxOccupants(Integer maxOccupants) {
        this.maxOccupants = maxOccupants;
    }

    public String getInternalNotes() {
        return internalNotes != null ? internalNotes : notes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
        if (this.notes == null) {
            this.notes = internalNotes;
        }
    }
}


