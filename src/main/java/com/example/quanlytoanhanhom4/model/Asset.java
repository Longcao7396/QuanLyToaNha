package com.example.quanlytoanhanhom4.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model cho quản lý thiết bị (Asset Management)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class Asset {
    private Integer id;
    private String assetCode; // Mã thiết bị
    private String assetName; // Tên thiết bị
    private String assetType; // Loại thiết bị: THANG_MÁY, MÁY_BƠM_NƯỚC, QUẠT_TĂNG_ÁP, PCCC, CAMERA, CỬA_CUỐN, MÁY_PHÁT_ĐIỆN, ĐÈN_CHIẾU_SÁNG, THIẾT_BỊ_ĐIỆN, THIẾT_BỊ_NƯỚC, KHÁC
    private String model; // Model thiết bị
    private String serialNumber; // Serial number
    private String location; // Vị trí lắp đặt (tòa – block – tầng – phòng kỹ thuật)
    private String buildingBlock; // Block/Tòa
    private String floor; // Tầng
    private String technicalRoom; // Phòng kỹ thuật
    private LocalDate installationDate; // Ngày lắp đặt
    private Integer expectedLifespan; // Tuổi thọ dự kiến (tháng)
    private String manufacturer; // Nhà sản xuất
    private String supplier; // Nhà cung cấp
    private String maintenanceProvider; // Đơn vị bảo trì
    private LocalDate warrantyStartDate; // Ngày bắt đầu bảo hành
    private Integer warrantyPeriod; // Thời gian bảo hành (tháng)
    private LocalDate warrantyEndDate; // Ngày hết hạn bảo hành
    private String status; // HOẠT_ĐỘNG, HỎNG, ĐANG_BẢO_TRÌ, NGƯNG_SỬ_DỤNG
    private Double assetValue; // Giá trị thiết bị (optional)
    private String documentationPath; // File tài liệu: manual, hợp đồng, hình ảnh
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Asset() {
    }

    public Asset(String assetCode, String assetName, String assetType, String location) {
        this.assetCode = assetCode;
        this.assetName = assetName;
        this.assetType = assetType;
        this.location = location;
        this.status = "HOẠT_ĐỘNG";
        this.installationDate = LocalDate.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public Integer getExpectedLifespan() {
        return expectedLifespan;
    }

    public void setExpectedLifespan(Integer expectedLifespan) {
        this.expectedLifespan = expectedLifespan;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocumentationPath() {
        return documentationPath;
    }

    public void setDocumentationPath(String documentationPath) {
        this.documentationPath = documentationPath;
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
    public boolean isOperational() {
        return "HOẠT_ĐỘNG".equals(status);
    }

    public boolean isBroken() {
        return "HỎNG".equals(status);
    }

    public boolean isUnderMaintenance() {
        return "ĐANG_BẢO_TRÌ".equals(status);
    }

    public boolean isWarrantyExpired() {
        if (warrantyEndDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(warrantyEndDate);
    }

    public boolean isWarrantyExpiringSoon(int days) {
        if (warrantyEndDate == null) {
            return false;
        }
        LocalDate warningDate = warrantyEndDate.minusDays(days);
        return LocalDate.now().isAfter(warningDate) && !isWarrantyExpired();
    }

    // Getters and Setters cho các trường mới
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBuildingBlock() {
        return buildingBlock;
    }

    public void setBuildingBlock(String buildingBlock) {
        this.buildingBlock = buildingBlock;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTechnicalRoom() {
        return technicalRoom;
    }

    public void setTechnicalRoom(String technicalRoom) {
        this.technicalRoom = technicalRoom;
    }

    public String getMaintenanceProvider() {
        return maintenanceProvider;
    }

    public void setMaintenanceProvider(String maintenanceProvider) {
        this.maintenanceProvider = maintenanceProvider;
    }

    public LocalDate getWarrantyStartDate() {
        return warrantyStartDate;
    }

    public void setWarrantyStartDate(LocalDate warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
        if (warrantyStartDate != null && warrantyPeriod != null) {
            this.warrantyEndDate = warrantyStartDate.plusMonths(warrantyPeriod);
        }
    }

    public LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }

    public void setWarrantyEndDate(LocalDate warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public Double getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(Double assetValue) {
        this.assetValue = assetValue;
    }
}

