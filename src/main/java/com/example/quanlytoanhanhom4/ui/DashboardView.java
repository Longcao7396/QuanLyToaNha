package com.example.quanlytoanhanhom4.ui;

import com.example.quanlytoanhanhom4.service.BMSService;
import com.example.quanlytoanhanhom4.service.MaintenanceService;
import com.example.quanlytoanhanhom4.service.SecurityService;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public final class DashboardView {
    private static Stage primaryStage;
    private static String currentRole;

    private DashboardView() {
        // Utility class
    }

    public static void show(Stage stage, String role) {
        primaryStage = stage;
        currentRole = role;

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Header với gradient đẹp hơn
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25));
        header.setStyle("-fx-background-color: linear-gradient(to right, #2874A6, #3498DB);");

        Label welcomeLabel = new Label("Hệ thống Quản lý Kỹ thuật Tòa Nhà");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label roleLabel = new Label("Vai trò: " + role.toUpperCase());
        roleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.9);");

        header.getChildren().addAll(welcomeLabel, roleLabel);
        root.setTop(header);

        // ScrollPane để chứa nội dung
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #f0f2f5;");

        // Statistics Cards
        HBox statsBox = createStatisticsCards();
        mainContent.getChildren().add(statsBox);

        // Charts Section
        HBox chartsBox = createChartsSection();
        mainContent.getChildren().add(chartsBox);

        // Module Buttons Section
        VBox modulesBox = createModulesSection();
        mainContent.getChildren().add(modulesBox);

        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        // Footer
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15, 30, 15, 30));
        footer.setStyle("-fx-background-color: #ffffff; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, -2);");

        Button backBtn = new Button("← Quay lại");
        backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 25; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;");
        backBtn.setOnAction(e -> handleBack());
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                "-fx-padding: 10 25; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-padding: 10 25; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 25; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;");
        logoutBtn.setOnAction(e -> handleLogout());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; " +
                "-fx-padding: 10 25; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-padding: 10 25; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        footer.getChildren().addAll(backBtn, logoutBtn);
        root.setBottom(footer);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Dashboard - Quản lý Kỹ thuật Tòa Nhà");
        primaryStage.setResizable(true);

        // Lấy kích thước màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Ẩn cửa sổ trước khi thay đổi kích thước để tránh giật
        boolean wasShowing = primaryStage.isShowing();
        if (wasShowing) {
            primaryStage.setOpacity(0.0); // Làm mờ trước khi ẩn để mượt hơn
            primaryStage.hide();
        }

        // Set maximize và kích thước trước khi set scene
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.setScene(scene);

        // Hiển thị cửa sổ và fade in để mượt hơn
        primaryStage.show();
        if (wasShowing) {
            primaryStage.setOpacity(1.0); // Fade in
        }

        // Đảm bảo cửa sổ được maximize
        javafx.application.Platform.runLater(() -> {
            primaryStage.setMaximized(true);
        });
    }

    private static HBox createStatisticsCards() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(0, 0, 20, 0));

        // Lấy dữ liệu thống kê
        int totalMaintenance = MaintenanceService.getAllMaintenances().size();
        int pendingMaintenance = MaintenanceService.getMaintenancesByStatus("PENDING").size();
        int completedMaintenance = MaintenanceService.getMaintenancesByStatus("COMPLETED").size();
        int totalBMS = BMSService.getAllSystems().size();
        int totalSecurity = SecurityService.getAllIncidents().size();
        int openSecurity = SecurityService.getIncidentsByStatus("OPEN").size();

        // Thêm dữ liệu mẫu nếu không có dữ liệu thực
        if (totalMaintenance == 0) {
            totalMaintenance = 20;
            pendingMaintenance = 5;
            completedMaintenance = 12;
        }
        if (totalBMS == 0) {
            totalBMS = 39;
        }
        if (totalSecurity == 0) {
            totalSecurity = 15;
            openSecurity = 3;
        }

        // Card 1: Bảo trì
        VBox card1 = createStatCard("Tổng Bảo trì", String.valueOf(totalMaintenance),
                "Đang chờ: " + pendingMaintenance, "#3498DB");
        // Card 2: BMS
        VBox card2 = createStatCard("Hệ thống BMS", String.valueOf(totalBMS),
                "Đang hoạt động", "#2ECC71");
        // Card 3: An ninh
        VBox card3 = createStatCard("Sự cố An ninh", String.valueOf(totalSecurity),
                "Đang mở: " + openSecurity, "#E74C3C");
        // Card 4: Hoàn thành
        VBox card4 = createStatCard("Đã hoàn thành", String.valueOf(completedMaintenance),
                "Bảo trì", "#9B59B6");

        statsBox.getChildren().addAll(card1, card2, card3, card4);
        return statsBox;
    }

    private static VBox createStatCard(String title, String value, String subtitle, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefWidth(250);
        card.setPrefHeight(150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6;");

        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        return card;
    }

    private static HBox createChartsSection() {
        HBox chartsBox = new HBox(20);
        chartsBox.setAlignment(Pos.CENTER);
        chartsBox.setPadding(new Insets(20, 0, 20, 0));

        // Pie Chart: Trạng thái Bảo trì
        PieChart maintenanceChart = createMaintenancePieChart();

        // Bar Chart: Hệ thống BMS theo loại
        BarChart<String, Number> bmsChart = createBMSBarChart();

        chartsBox.getChildren().addAll(maintenanceChart, bmsChart);
        return chartsBox;
    }

    private static PieChart createMaintenancePieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Trạng thái Bảo trì");
        pieChart.setPrefWidth(500);
        pieChart.setPrefHeight(400);
        pieChart.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        int pending = MaintenanceService.getMaintenancesByStatus("PENDING").size();
        int inProgress = MaintenanceService.getMaintenancesByStatus("IN_PROGRESS").size();
        int completed = MaintenanceService.getMaintenancesByStatus("COMPLETED").size();

        // Thêm dữ liệu mẫu nếu không có dữ liệu thực
        if (pending == 0 && inProgress == 0 && completed == 0) {
            pending = 5;
            inProgress = 3;
            completed = 12;
        }

        PieChart.Data pendingData = new PieChart.Data("Đang chờ", pending);
        PieChart.Data inProgressData = new PieChart.Data("Đang thực hiện", inProgress);
        PieChart.Data completedData = new PieChart.Data("Hoàn thành", completed);

        pieChart.getData().addAll(pendingData, inProgressData, completedData);

        // Tùy chỉnh màu sắc sau khi chart được render
        javafx.application.Platform.runLater(() -> {
            if (pieChart.getData().size() > 0 && pieChart.getData().get(0).getNode() != null) {
                pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #F39C12;");
            }
            if (pieChart.getData().size() > 1 && pieChart.getData().get(1).getNode() != null) {
                pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #3498DB;");
            }
            if (pieChart.getData().size() > 2 && pieChart.getData().get(2).getNode() != null) {
                pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #2ECC71;");
            }
        });

        return pieChart;
    }

    private static BarChart<String, Number> createBMSBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Hệ thống BMS theo Loại");
        barChart.setPrefWidth(500);
        barChart.setPrefHeight(400);
        barChart.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        barChart.setLegendVisible(false);

        xAxis.setLabel("Loại hệ thống");
        yAxis.setLabel("Số lượng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Đếm hệ thống theo loại
        java.util.List<com.example.quanlytoanhanhom4.model.BMSSystem> allSystems = BMSService.getAllSystems();
        java.util.Map<String, Long> systemCounts = allSystems.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        system -> system.getSystemType() != null ? system.getSystemType() : "Khác",
                        java.util.stream.Collectors.counting()
                ));

        // Thêm dữ liệu mẫu nếu không có dữ liệu thực
        if (systemCounts.isEmpty()) {
            systemCounts.put("ĐIỆN", 8L);
            systemCounts.put("NƯỚC", 6L);
            systemCounts.put("HVAC", 5L);
            systemCounts.put("PCCC", 4L);
            systemCounts.put("AN_NINH", 7L);
            systemCounts.put("CHIEU_SANG", 9L);
        }

        for (java.util.Map.Entry<String, Long> entry : systemCounts.entrySet()) {
            // Chuyển đổi tên loại sang tiếng Việt
            String displayName = convertSystemTypeToVietnamese(entry.getKey());
            series.getData().add(new XYChart.Data<>(displayName, entry.getValue()));
        }

        barChart.getData().add(series);

        // Tùy chỉnh màu cột sau khi chart được render
        javafx.application.Platform.runLater(() -> {
            if (!barChart.getData().isEmpty() && !barChart.getData().get(0).getData().isEmpty()) {
                barChart.getData().get(0).getData().forEach(data -> {
                    if (data.getNode() != null) {
                        data.getNode().setStyle("-fx-bar-fill: #3498DB;");
                    }
                });
            }
        });

        return barChart;
    }

    private static VBox createModulesSection() {
        VBox modulesBox = new VBox(20);
        modulesBox.setAlignment(Pos.CENTER);
        modulesBox.setPadding(new Insets(20));

        Label sectionLabel = new Label("Quản lý Hệ thống Kỹ thuật");
        sectionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        Button bmsBtn = new Button("Giám sát & Điều khiển (BMS)");
        Button maintenanceBtn = new Button("Bảo trì & Bảo dưỡng");
        Button securityBtn = new Button("Quản lý An ninh");
        Button cleaningBtn = new Button("Quản lý Vệ sinh");
        Button adminBtn = new Button("Quản lý Hành chính & Nhân sự");
        Button hrBtn = new Button("Nhân sự & Chấm công");
        Button customerBtn = new Button("Quản lý Khách hàng");

        styleButton(bmsBtn);
        styleButton(maintenanceBtn);
        styleButton(securityBtn);
        styleButton(cleaningBtn);
        styleButton(adminBtn);
        styleButton(hrBtn);
        styleButton(customerBtn);

        bmsBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/bms.fxml", "Giám sát & Điều khiển BMS"));
        maintenanceBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/maintenance.fxml", "Bảo trì & Bảo dưỡng"));
        securityBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/security.fxml", "Quản lý An ninh"));
        cleaningBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/cleaning.fxml", "Quản lý Vệ sinh"));
        adminBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/admin.fxml", "Quản lý Hành chính & Nhân sự"));
        hrBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/hr.fxml", "Nhân sự & Chấm công"));
        customerBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/customer.fxml", "Quản lý Khách hàng"));

        grid.add(bmsBtn, 0, 0);
        grid.add(maintenanceBtn, 1, 0);
        grid.add(securityBtn, 2, 0);
        grid.add(cleaningBtn, 0, 1);
        grid.add(adminBtn, 1, 1);
        grid.add(hrBtn, 2, 1);
        grid.add(customerBtn, 0, 2);

        modulesBox.getChildren().addAll(sectionLabel, grid);
        return modulesBox;
    }

    private static void styleButton(Button button) {
        button.setPrefWidth(250);
        button.setPrefHeight(60);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498DB, #2874A6); " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: linear-gradient(to bottom, #2874A6, #1a5a8a); " +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 3);"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: linear-gradient(to bottom, #3498DB, #2874A6); " +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"));
    }

    private static void openModule(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(DashboardView.class.getResource(fxmlPath));
            Stage moduleStage = new Stage();
            Scene scene = new Scene(loader.load());
            moduleStage.setTitle(title);
            moduleStage.setScene(scene);
            moduleStage.setResizable(true);
            moduleStage.setMaximized(true);
            moduleStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Lỗi khi mở module: " + e.getMessage());
        }
    }

    private static String convertSystemTypeToVietnamese(String systemType) {
        java.util.Map<String, String> typeMap = new java.util.HashMap<>();
        typeMap.put("ĐIỆN", "Điện");
        typeMap.put("NƯỚC", "Nước");
        typeMap.put("HVAC", "HVAC (Điều hòa)");
        typeMap.put("PCCC", "Phòng cháy chữa cháy");
        typeMap.put("AN_NINH", "An ninh");
        typeMap.put("CHIEU_SANG", "Chiếu sáng");
        return typeMap.getOrDefault(systemType, systemType);
    }

    private static void showMessage(String message) {
        Stage messageStage = new Stage();
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Label label = new Label(message);
        Button okBtn = new Button("OK");
        okBtn.setOnAction(e -> messageStage.close());

        vbox.getChildren().addAll(label, okBtn);
        Scene scene = new Scene(vbox, 350, 150);
        messageStage.setScene(scene);
        messageStage.setTitle("Thông báo");
        messageStage.show();
    }

    private static void handleBack() {
        try {
            if (primaryStage != null) {
                // Quay lại màn hình chính (main.fxml)
                FXMLLoader loader = new FXMLLoader(DashboardView.class.getResource("/com/example/quanlytoanhanhom4/fxml/main.fxml"));
                Scene scene = new Scene(loader.load(), 1080, 640);
                primaryStage.setTitle("Quản lý kỹ thuật tòa nhà");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Không thể quay lại màn hình chính: " + e.getMessage());
        }
    }

    private static void handleLogout() {
        try {
            if (primaryStage != null) {
                primaryStage.close();
            }
            UserSession.clear();

            FXMLLoader loader = new FXMLLoader(DashboardView.class.getResource("/com/example/quanlytoanhanhom4/fxml/login.fxml"));
            Stage loginStage = new Stage();
            Scene scene = new Scene(loader.load(), 400, 350);
            loginStage.setTitle("Đăng nhập quản lý toà nhà");
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Không thể quay lại màn hình đăng nhập: " + e.getMessage());
        }
    }
}
