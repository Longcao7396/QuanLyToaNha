package com.example.quanlytoanhanhom4.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class để hỗ trợ phân trang cho TableView
 * Áp dụng kiến thức từ JP1&JP2 về cấu trúc dữ liệu và giải thuật
 */
public class PaginationHelper {

    /**
     * Tạo Pagination cho TableView với số bản ghi mỗi trang
     * 
     * @param tableView TableView cần phân trang
     * @param allItems Danh sách tất cả items
     * @param itemsPerPage Số items mỗi trang (20-50)
     * @return Pagination control
     */
    public static <T> Pagination createPagination(
            TableView<T> tableView,
            ObservableList<T> allItems,
            int itemsPerPage) {
        
        // Tạo biến final để sử dụng trong lambda
        final int finalItemsPerPage = itemsPerPage < 1 ? 20 : itemsPerPage;
        
        int totalItems = allItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / finalItemsPerPage);
        
        if (totalPages == 0) {
            totalPages = 1;
        }
        
        Pagination pagination = new Pagination(totalPages, 0);
        pagination.setPageCount(totalPages);
        pagination.setMaxPageIndicatorCount(10);
        
        // Cập nhật table khi đổi trang
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            int fromIndex = newIndex.intValue() * finalItemsPerPage;
            int toIndex = Math.min(fromIndex + finalItemsPerPage, totalItems);
            
            ObservableList<T> pageItems = FXCollections.observableArrayList();
            if (fromIndex < totalItems) {
                pageItems.addAll(allItems.subList(fromIndex, toIndex));
            }
            tableView.setItems(pageItems);
        });
        
        // Load trang đầu tiên
        int fromIndex = 0;
        int toIndex = Math.min(finalItemsPerPage, totalItems);
        if (fromIndex < totalItems) {
            ObservableList<T> pageItems = FXCollections.observableArrayList();
            pageItems.addAll(allItems.subList(fromIndex, toIndex));
            tableView.setItems(pageItems);
        }
        
        return pagination;
    }

    /**
     * Cập nhật pagination khi danh sách thay đổi
     */
    public static <T> void updatePagination(
            Pagination pagination,
            TableView<T> tableView,
            ObservableList<T> allItems,
            int itemsPerPage) {
        
        int totalItems = allItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        
        if (totalPages == 0) {
            totalPages = 1;
        }
        
        pagination.setPageCount(totalPages);
        
        int currentPage = pagination.getCurrentPageIndex();
        int fromIndex = currentPage * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        
        ObservableList<T> pageItems = FXCollections.observableArrayList();
        if (fromIndex < totalItems) {
            pageItems.addAll(allItems.subList(fromIndex, toIndex));
        }
        tableView.setItems(pageItems);
    }

    /**
     * Tạo pagination với tùy chọn số items mỗi trang
     */
    public static <T> Pagination createPaginationWithOptions(
            TableView<T> tableView,
            ObservableList<T> allItems,
            int defaultItemsPerPage) {
        
        return createPagination(tableView, allItems, defaultItemsPerPage);
    }
}

