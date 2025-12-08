package com.example.quanlytoanhanhom4.model;

/**
 * Model cho chi tiết hóa đơn (dòng phí trong hóa đơn)
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 3. Hóa đơn - Cấu trúc hóa đơn: Danh sách phí (nhiều dòng)
 */
public class InvoiceItem {
    private Integer id;
    private Integer invoiceId; // ID hóa đơn
    private Integer feeTypeId; // Loại phí
    private String feeName; // Tên phí (để hiển thị nhanh, không cần join)
    private String description; // Mô tả chi tiết
    private Double quantity; // Số lượng (diện tích, số xe, số kWh, etc.)
    private String unit; // Đơn vị
    private Double unitPrice; // Đơn giá
    private Double amount; // Thành tiền
    private Integer apartmentServiceFeeId; // Liên kết với ApartmentServiceFee (nếu có)
    private String notes;

    public InvoiceItem() {
    }

    public InvoiceItem(Integer invoiceId, Integer feeTypeId, String feeName, Double quantity, Double unitPrice) {
        this.invoiceId = invoiceId;
        this.feeTypeId = feeTypeId;
        this.feeName = feeName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.calculateAmount();
    }

    /**
     * Tính thành tiền
     */
    public void calculateAmount() {
        if (quantity != null && unitPrice != null) {
            this.amount = quantity * unitPrice;
        } else if (unitPrice != null) {
            this.amount = unitPrice; // Phí cố định
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
        this.calculateAmount();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        this.calculateAmount();
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getApartmentServiceFeeId() {
        return apartmentServiceFeeId;
    }

    public void setApartmentServiceFeeId(Integer apartmentServiceFeeId) {
        this.apartmentServiceFeeId = apartmentServiceFeeId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

