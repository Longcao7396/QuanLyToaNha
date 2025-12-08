package com.example.quanlytoanhanhom4.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility class để hỗ trợ tìm kiếm cho TableView
 * Áp dụng kiến thức từ JP1&JP2 về tìm kiếm và lọc dữ liệu
 */
public class SearchHelper {

    /**
     * Tạo TextField tìm kiếm với filter tự động
     * 
     * @param searchField TextField để nhập từ khóa
     * @param allItems Danh sách tất cả items
     * @param filterPredicate Predicate để lọc items
     * @return FilteredList đã được lọc
     */
    public static <T> FilteredList<T> createSearchFilter(
            TextField searchField,
            ObservableList<T> allItems,
            Predicate<T> filterPredicate) {
        
        FilteredList<T> filteredList = new FilteredList<>(allItems, p -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                filteredList.setPredicate(p -> true);
            } else {
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(item -> {
                    // Kết hợp với predicate từ bên ngoài
                    if (filterPredicate != null && !filterPredicate.test(item)) {
                        return false;
                    }
                    // Tìm kiếm sẽ được xử lý bởi predicate riêng
                    return true;
                });
            }
        });
        
        return filteredList;
    }

    /**
     * Tìm kiếm trong danh sách với nhiều tiêu chí
     * Sử dụng Linear Search từ JP1&JP2
     */
    public static <T> ObservableList<T> search(
            ObservableList<T> items,
            String searchText,
            Function<T, String>... searchFields) {
        
        if (searchText == null || searchText.trim().isEmpty()) {
            return items;
        }
        
        String lowerSearchText = searchText.toLowerCase().trim();
        ObservableList<T> results = FXCollections.observableArrayList();
        
        for (T item : items) {
            boolean matches = false;
            for (Function<T, String> field : searchFields) {
                String fieldValue = field.apply(item);
                if (fieldValue != null && fieldValue.toLowerCase().contains(lowerSearchText)) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                results.add(item);
            }
        }
        
        return results;
    }

    /**
     * Tìm kiếm theo tên và số điện thoại (cho Resident)
     */
    public static <T> Predicate<T> createNameAndPhonePredicate(
            String searchText,
            Function<T, String> nameExtractor,
            Function<T, String> phoneExtractor) {
        
        if (searchText == null || searchText.trim().isEmpty()) {
            return item -> true;
        }
        
        String lowerSearchText = searchText.toLowerCase().trim();
        
        return item -> {
            String name = nameExtractor.apply(item);
            String phone = phoneExtractor.apply(item);
            
            boolean nameMatch = name != null && name.toLowerCase().contains(lowerSearchText);
            boolean phoneMatch = phone != null && phone.contains(lowerSearchText);
            
            return nameMatch || phoneMatch;
        };
    }

    /**
     * Tạo FilteredList với tìm kiếm theo nhiều trường
     */
    public static <T> FilteredList<T> createMultiFieldSearch(
            TextField searchField,
            ObservableList<T> allItems,
            Function<T, String>... searchFields) {
        
        FilteredList<T> filteredList = new FilteredList<>(allItems, p -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                filteredList.setPredicate(p -> true);
            } else {
                String lowerCaseFilter = newValue.toLowerCase().trim();
                filteredList.setPredicate(item -> {
                    for (Function<T, String> field : searchFields) {
                        String fieldValue = field.apply(item);
                        if (fieldValue != null && fieldValue.toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        });
        
        return filteredList;
    }
}

