package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho ra vào bãi xe – barrier (Parking Gate / Barrier Control)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class ParkingEntry {
    private Integer id;
    private String entryCode; // Mã vào/ra
    private String licensePlate; // Biển số đã nhận diện
    private Integer residentId; // Cư dân sở hữu xe (nếu có)
    private Integer visitorId; // Khách (nếu là xe khách)
    private String vehicleType; // Loại xe: XE_CƯ_DÂN, XE_KHÁCH, XE_GIAO_HÀNG
    private String entryMethod; // Phương thức vào: ANPR, THẺ, QR_CODE, THỦ_CÔNG
    private LocalDateTime entryTime; // Thời gian vào
    private LocalDateTime exitTime; // Thời gian ra
    private String entryGate; // Cổng vào
    private String exitGate; // Cổng ra
    private String cameraImagePath; // Hình ảnh camera kèm theo
    private String accessCardCode; // Mã thẻ đã dùng (nếu dùng thẻ)
    private String qrCode; // Mã QR đã dùng (nếu dùng QR)
    private Double parkingFee; // Phí gửi xe (nếu có)
    private String status; // Trạng thái: ĐANG_TRONG, ĐÃ_RA, BẤT_THƯỜNG
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ParkingEntry() {
    }

    public ParkingEntry(String licensePlate, String vehicleType, String entryMethod) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.entryMethod = entryMethod;
        this.entryTime = LocalDateTime.now();
        this.status = "ĐANG_TRONG";
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getEntryGate() {
        return entryGate;
    }

    public void setEntryGate(String entryGate) {
        this.entryGate = entryGate;
    }

    public String getExitGate() {
        return exitGate;
    }

    public void setExitGate(String exitGate) {
        this.exitGate = exitGate;
    }

    public String getCameraImagePath() {
        return cameraImagePath;
    }

    public void setCameraImagePath(String cameraImagePath) {
        this.cameraImagePath = cameraImagePath;
    }

    public String getAccessCardCode() {
        return accessCardCode;
    }

    public void setAccessCardCode(String accessCardCode) {
        this.accessCardCode = accessCardCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Double getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(Double parkingFee) {
        this.parkingFee = parkingFee;
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

    // Helper methods
    public boolean isInside() {
        return "ĐANG_TRONG".equals(status);
    }

    public long getParkingDurationMinutes() {
        if (entryTime == null) {
            return 0;
        }
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return java.time.Duration.between(entryTime, endTime).toMinutes();
    }
}

