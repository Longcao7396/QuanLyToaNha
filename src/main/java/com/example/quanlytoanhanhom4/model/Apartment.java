package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

public class Apartment {
    private int id;
    private Integer residentOwnerId;
    private String apartmentNo;
    private Integer numberOfRooms;
    private Integer numberOfPeople;
    private Double area;
    private Double price;
    private Integer floorNumber;
    private String buildingBlock;
    private String status; // OCCUPIED, VACANT, RESERVED, MAINTENANCE
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Apartment() {
    }

    public Apartment(String apartmentNo, Integer numberOfRooms, Double area, Double price) {
        this.apartmentNo = apartmentNo;
        this.numberOfRooms = numberOfRooms;
        this.area = area;
        this.price = price;
        this.status = "VACANT";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getResidentOwnerId() {
        return residentOwnerId;
    }

    public void setResidentOwnerId(Integer residentOwnerId) {
        this.residentOwnerId = residentOwnerId;
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

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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


