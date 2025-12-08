package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;

/**
 * Model cho cư dân/hộ gia đình
 * Module 1: Quản lý cư dân/hộ gia đình
 */
public class Resident {
    private Integer id;
    private Integer userId;
    private String fullName;
    private String identityCard;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;
    private String residentType; // Loại cư dân: CHỦ_HỘ, NGƯỜI_THUÊ, NGƯỜI_THÂN, TRẺ_EM, NGƯỜI_GIÚP_VIỆC
    private String relationshipType; // CHỦ_HỘ, THÀNH_VIÊN, TẠM_TRÚ (giữ lại để tương thích)
    private Integer householdId;
    private Integer apartmentId;
    private Boolean isHouseholdHead;
    private LocalDate moveInDate; // Ngày chuyển vào
    private LocalDate expectedMoveOutDate; // Ngày dự kiến rời đi (nếu thuê)
    private String vehicleLicensePlate; // Biển số xe (nếu có)
    private String profilePhoto; // Đường dẫn ảnh đại diện
    private String status; // ĐANG_Ở, ĐÃ_CHUYỂN_ĐI
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Resident() {
    }

    public Resident(Integer userId, String fullName, String identityCard, Integer apartmentId) {
        this.userId = userId;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.apartmentId = apartmentId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Integer getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(Integer householdId) {
        this.householdId = householdId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public Boolean getIsHouseholdHead() {
        return isHouseholdHead;
    }

    public void setIsHouseholdHead(Boolean isHouseholdHead) {
        this.isHouseholdHead = isHouseholdHead;
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

    public String getResidentType() {
        return residentType != null ? residentType : relationshipType;
    }

    public void setResidentType(String residentType) {
        this.residentType = residentType;
        if (this.relationshipType == null) {
            this.relationshipType = residentType;
        }
    }

    public LocalDate getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(LocalDate moveInDate) {
        this.moveInDate = moveInDate;
    }

    public LocalDate getExpectedMoveOutDate() {
        return expectedMoveOutDate;
    }

    public void setExpectedMoveOutDate(LocalDate expectedMoveOutDate) {
        this.expectedMoveOutDate = expectedMoveOutDate;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
