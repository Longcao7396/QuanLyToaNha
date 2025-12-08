package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model cho tòa nhà / Block
 * Module 1: Quản lý cư dân & căn hộ
 * 1.1. Cấu trúc tòa nhà → block → tầng → căn hộ
 */
public class Building {
    private Integer id;
    private String buildingName; // Tên tòa nhà
    private String blockCode; // Mã block (A, B, C)
    private Integer totalFloors; // Tổng số tầng
    private String address; // Địa chỉ tòa nhà
    private String description; // Mô tả
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Danh sách căn hộ (không lưu trong DB, chỉ để hiển thị)
    private List<Apartment> apartments;

    public Building() {
    }

    public Building(String buildingName, String blockCode, Integer totalFloors) {
        this.buildingName = buildingName;
        this.blockCode = blockCode;
        this.totalFloors = totalFloors;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
    }
}

