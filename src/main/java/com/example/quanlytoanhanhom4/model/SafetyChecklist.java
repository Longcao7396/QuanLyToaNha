package com.example.quanlytoanhanhom4.model;

import java.time.LocalDateTime;

/**
 * Model cho kiểm tra an toàn (Safety Checklist)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class SafetyChecklist {
    private Integer id;
    private String checklistCode; // Mã checklist
    private String checklistType; // Loại checklist: THANG_MÁY, PHÒNG_MÁY_BƠM, TỦ_ĐIỆN, PCCC, KHÁC
    private String location; // Vị trí kiểm tra
    private Integer checkedBy; // Người kiểm tra (user_id)
    private LocalDateTime checkDate; // Ngày giờ kiểm tra
    private String checkItems; // Danh mục mục kiểm tra (JSON hoặc text)
    private String checkResults; // Kết quả kiểm tra (JSON: {item: "Đạt"/"Không đạt"/"Cần theo dõi"})
    private String imagePath; // Hình ảnh chứng minh
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SafetyChecklist() {
    }

    public SafetyChecklist(String checklistType, String location, Integer checkedBy) {
        this.checklistType = checklistType;
        this.location = location;
        this.checkedBy = checkedBy;
        this.checkDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChecklistCode() {
        return checklistCode;
    }

    public void setChecklistCode(String checklistCode) {
        this.checklistCode = checklistCode;
    }

    public String getChecklistType() {
        return checklistType;
    }

    public void setChecklistType(String checklistType) {
        this.checklistType = checklistType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(Integer checkedBy) {
        this.checkedBy = checkedBy;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(String checkItems) {
        this.checkItems = checkItems;
    }

    public String getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(String checkResults) {
        this.checkResults = checkResults;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
}

