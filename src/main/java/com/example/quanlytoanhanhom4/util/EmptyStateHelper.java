package com.example.quanlytoanhanhom4.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Utility class để tạo empty state cho TableView
 * Hiển thị thông báo đẹp khi bảng chưa có dữ liệu
 */
public class EmptyStateHelper {

    /**
     * Tạo empty state cho TableView với icon, message và button
     * 
     * @param tableView TableView cần set empty state
     * @param icon Emoji hoặc text icon
     * @param title Tiêu đề
     * @param message Thông báo
     * @param buttonText Text cho nút (null nếu không muốn hiển thị nút)
     * @param onButtonClick Action khi click nút (null nếu không có nút)
     */
    public static <T> void setEmptyState(
            TableView<T> tableView,
            String icon,
            String title,
            String message,
            String buttonText,
            Runnable onButtonClick) {
        
        StackPane emptyStatePane = new StackPane();
        emptyStatePane.setPrefHeight(300);
        emptyStatePane.setAlignment(Pos.CENTER);
        
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));
        contentBox.setMaxWidth(400);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(new Font(64));
        iconLabel.setStyle("-fx-text-fill: #bdc3c7;");
        
        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font(20));
        titleLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        
        // Message
        Label messageLabel = new Label(message);
        messageLabel.setFont(new Font(14));
        messageLabel.setStyle("-fx-text-fill: #95a5a6;");
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setWrapText(true);
        
        contentBox.getChildren().addAll(iconLabel, titleLabel, messageLabel);
        
        // Button (nếu có)
        if (buttonText != null && onButtonClick != null) {
            Button actionButton = new Button(buttonText);
            actionButton.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 12 24; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            );
            actionButton.setOnAction(e -> onButtonClick.run());
            contentBox.getChildren().add(actionButton);
        }
        
        emptyStatePane.getChildren().add(contentBox);
        
        // Set placeholder
        tableView.setPlaceholder(emptyStatePane);
    }

    /**
     * Tạo empty state đơn giản chỉ với message
     */
    public static <T> void setSimpleEmptyState(TableView<T> tableView, String message) {
        setEmptyState(tableView, "📋", "Chưa có dữ liệu", message, null, null);
    }

    /**
     * Tạo empty state cho quản lý cư dân
     */
    public static <T> void setResidentEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "👥",
            "Chưa có cư dân",
            "Chưa có dữ liệu cư dân.\nBấm \"Thêm cư dân\" để tạo cư dân đầu tiên.",
            "➕ Thêm cư dân",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý căn hộ
     */
    public static <T> void setApartmentEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "🏠",
            "Chưa có căn hộ",
            "Chưa có dữ liệu căn hộ.\nBấm \"Thêm căn hộ\" để tạo căn hộ đầu tiên.",
            "➕ Thêm căn hộ",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý hóa đơn
     */
    public static <T> void setInvoiceEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "🧾",
            "Chưa có hóa đơn",
            "Chưa có dữ liệu hóa đơn.\nBấm \"Tạo hóa đơn\" để tạo hóa đơn đầu tiên.",
            "➕ Tạo hóa đơn",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý ticket
     */
    public static <T> void setTicketEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "🎫",
            "Chưa có yêu cầu",
            "Chưa có yêu cầu nào.\nBấm \"Tạo yêu cầu\" để tạo yêu cầu đầu tiên.",
            "➕ Tạo yêu cầu",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý thiết bị
     */
    public static <T> void setAssetEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "⚙️",
            "Chưa có thiết bị",
            "Chưa có dữ liệu thiết bị.\nBấm \"Thêm thiết bị\" để thêm thiết bị đầu tiên.",
            "➕ Thêm thiết bị",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý thông báo
     */
    public static <T> void setNotificationEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "🔔",
            "Chưa có thông báo",
            "Chưa có thông báo nào.\nBấm \"Tạo thông báo\" để tạo thông báo đầu tiên.",
            "➕ Tạo thông báo",
            onAddClick
        );
    }

    /**
     * Tạo empty state cho quản lý phí dịch vụ & điện nước
     */
    public static <T> void setServiceFeeEmptyState(TableView<T> tableView, Runnable onAddClick) {
        setEmptyState(
            tableView,
            "⚡",
            "Chưa có phí dịch vụ",
            "Chưa có dữ liệu phí dịch vụ.\nBấm \"Thêm phí dịch vụ\" để tạo phí dịch vụ đầu tiên.",
            "➕ Thêm phí dịch vụ",
            onAddClick
        );
    }

    /**
     * Tạo empty state tùy chỉnh
     */
    public static <T> void setCustomEmptyState(
            TableView<T> tableView,
            String icon,
            String title,
            String message,
            String buttonText,
            Runnable onButtonClick) {
        setEmptyState(tableView, icon, title, message, buttonText, onButtonClick);
    }
}

