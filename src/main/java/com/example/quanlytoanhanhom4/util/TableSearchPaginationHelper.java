package com.example.quanlytoanhanhom4.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

/**
 * Helper class để tích hợp tìm kiếm, phân trang và bộ lọc cho TableView
 * Áp dụng kiến thức từ JP1&JP2 về cấu trúc dữ liệu và giải thuật
 */
public class TableSearchPaginationHelper<T> {
    
    private TableView<T> tableView;
    private ObservableList<T> allItems;
    private FilteredList<T> filteredItems;
    private Pagination pagination;
    private TextField searchField;
    private ComboBox<Integer> itemsPerPageCombo;
    private int itemsPerPage;
    private Predicate<T> customFilter;
    
    public TableSearchPaginationHelper(
            TableView<T> tableView,
            ObservableList<T> allItems,
            Pagination pagination,
            TextField searchField,
            ComboBox<Integer> itemsPerPageCombo) {
        
        this.tableView = tableView;
        this.allItems = allItems;
        this.pagination = pagination;
        this.searchField = searchField;
        this.itemsPerPageCombo = itemsPerPageCombo;
        this.itemsPerPage = 20; // Default
        
        initialize();
    }
    
    private void initialize() {
        // Tạo filtered list
        filteredItems = new FilteredList<>(allItems, p -> true);
        
        // Setup search field listener
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
        
        // Setup items per page combo
        if (itemsPerPageCombo != null) {
            itemsPerPageCombo.setItems(FXCollections.observableArrayList(20, 30, 50, 100));
            itemsPerPageCombo.setValue(20);
            itemsPerPageCombo.setOnAction(e -> {
                itemsPerPage = itemsPerPageCombo.getValue();
                updatePagination();
            });
        }
        
        // Initial pagination
        updatePagination();
    }
    
    /**
     * Set custom filter predicate
     */
    public void setCustomFilter(Predicate<T> filter) {
        this.customFilter = filter;
        applyFilters();
    }
    
    /**
     * Áp dụng tất cả filters
     */
    public void applyFilters() {
        if (allItems == null) {
            return;
        }
        
        filteredItems.setPredicate(item -> {
            // Apply custom filter if exists
            if (customFilter != null && !customFilter.test(item)) {
                return false;
            }
            
            // Apply search filter if exists
            if (searchField != null) {
                String searchText = searchField.getText();
                if (searchText != null && !searchText.trim().isEmpty()) {
                    // Search will be handled by custom filter or override this method
                    // Default: return true (no search)
                }
            }
            
            return true;
        });
        
        updatePagination();
    }
    
    /**
     * Cập nhật pagination
     */
    public void updatePagination() {
        if (filteredItems == null || pagination == null) {
            return;
        }
        
        ObservableList<T> itemsToPaginate = FXCollections.observableArrayList(filteredItems);
        PaginationHelper.updatePagination(pagination, tableView, itemsToPaginate, itemsPerPage);
    }
    
    /**
     * Reload data
     */
    public void reloadData(ObservableList<T> newData) {
        allItems.clear();
        allItems.addAll(newData);
        applyFilters();
    }
    
    /**
     * Get filtered items
     */
    public FilteredList<T> getFilteredItems() {
        return filteredItems;
    }
    
    /**
     * Get all items
     */
    public ObservableList<T> getAllItems() {
        return allItems;
    }
}








