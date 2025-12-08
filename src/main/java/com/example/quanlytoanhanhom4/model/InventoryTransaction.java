package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho lịch sử nhập xuất kho (Inventory Transaction)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class InventoryTransaction {
    private Integer id;
    private String transactionCode; // Mã phiếu nhập/xuất
    private Integer resourceId; // Vật tư
    private String transactionType; // Loại giao dịch: NHẬP_KHO, XUẤT_KHO, KIỂM_KHO, ĐIỀU_CHUYỂN
    private Integer quantity; // Số lượng
    private Double unitPrice; // Đơn giá tại thời điểm giao dịch
    private Double totalAmount; // Tổng tiền
    private String referenceType; // Loại tham chiếu: PREVENTIVE_MAINTENANCE, CORRECTIVE_MAINTENANCE, TICKET, MANUAL, OTHER
    private Integer referenceId; // ID của tham chiếu (PM/CM/Ticket ID)
    private String supplier; // Nhà cung cấp (nếu là nhập kho)
    private String recipient; // Người nhận (nếu là xuất kho)
    private Integer performedBy; // Người thực hiện (user_id)
    private String warehouseName; // Tên kho
    private String notes; // Ghi chú
    private LocalDateTime transactionDate; // Ngày giao dịch
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryTransaction() {
    }

    public InventoryTransaction(Integer resourceId, String transactionType, Integer quantity) {
        this.resourceId = resourceId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalAmount() {
        if (totalAmount != null) {
            return totalAmount;
        }
        // Tự động tính nếu chưa set
        if (quantity != null && unitPrice != null) {
            return quantity * unitPrice;
        }
        return 0.0;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Integer getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Integer performedBy) {
        this.performedBy = performedBy;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
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
    public boolean isInbound() {
        return "NHẬP_KHO".equals(transactionType);
    }

    public boolean isOutbound() {
        return "XUẤT_KHO".equals(transactionType);
    }

    public boolean isStockCheck() {
        return "KIỂM_KHO".equals(transactionType);
    }
}

