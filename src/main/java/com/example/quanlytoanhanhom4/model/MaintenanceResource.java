package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho vật tư & chi phí sửa chữa (Maintenance Resource)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class MaintenanceResource {
    private Integer id;
    private String resourceCode; // Mã vật tư
    private String resourceName; // Tên vật tư
    private String resourceType; // Loại: BÓNG_ĐÈN, DÂY_ĐIỆN, ỐNG_NƯỚC, KHỚP_NỐI, CẦU_CHÌ, APTOMAT, DẦU_MÁY, LỌC_GIÓ, THIẾT_BỊ_PCCC, BULONG_ỐC_VÍT, PHỤ_KIỆN, KHÁC
    private String unit; // Đơn vị tính: CÁI, MÉT, KG, THÙNG, LÍT, KHÁC
    private Integer quantityInStock; // Số lượng tồn hiện tại
    private Integer minStockLevel; // Định mức tồn kho tối thiểu
    private Double purchasePrice; // Giá nhập
    private Double salePrice; // Giá xuất
    private String supplier; // Nhà cung cấp
    private String warehouseLocation; // Vị trí trong kho
    private String warehouseName; // Tên kho (kho kỹ thuật, kho PCCC, etc.)
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MaintenanceResource() {
    }

    public MaintenanceResource(String resourceCode, String resourceName, String resourceType, String unit) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.unit = unit;
        this.quantityInStock = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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
    public boolean isLowStock() {
        if (minStockLevel == null || quantityInStock == null) {
            return false;
        }
        return quantityInStock <= minStockLevel;
    }

    public Double getTotalValue() {
        if (quantityInStock == null || purchasePrice == null) {
            return 0.0;
        }
        return quantityInStock * purchasePrice;
    }

    // Getters and Setters cho các trường mới
    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    // Deprecated - giữ lại để backward compatibility
    @Deprecated
    public Double getUnitPrice() {
        return purchasePrice;
    }

    @Deprecated
    public void setUnitPrice(Double unitPrice) {
        this.purchasePrice = unitPrice;
    }

    @Deprecated
    public String getLocation() {
        return warehouseLocation;
    }

    @Deprecated
    public void setLocation(String location) {
        this.warehouseLocation = location;
    }
}

