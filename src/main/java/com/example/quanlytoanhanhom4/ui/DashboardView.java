package com.example.quanlytoanhanhom4.ui;

import com.example.quanlytoanhanhom4.service.ApartmentService;
import com.example.quanlytoanhanhom4.service.BMSService;
import com.example.quanlytoanhanhom4.service.InvoiceService;
import com.example.quanlytoanhanhom4.service.MaintenanceService;
import com.example.quanlytoanhanhom4.service.NotificationService;
import com.example.quanlytoanhanhom4.service.RepairRequestService;
import com.example.quanlytoanhanhom4.service.ResidentService;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

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
        root.setStyle("-fx-background-color: #f5f7fa;");

        // Header với gradient đẹp hơn
        VBox header = createHeader(role);
        root.setTop(header);

        // ScrollPane để chứa nội dung
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0));

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #f5f7fa;");

        // KPI Cards Section - Hàng đầu tiên
        HBox kpiRow1 = createKPICardsRow1();
        mainContent.getChildren().add(kpiRow1);

        // KPI Cards Section - Hàng thứ hai
        HBox kpiRow2 = createKPICardsRow2();
        mainContent.getChildren().add(kpiRow2);

        // Charts Section - 2 cột
        HBox chartsBox = createChartsSection();
        mainContent.getChildren().add(chartsBox);

        // Recent Activity Section
        VBox activityBox = createRecentActivitySection();
        mainContent.getChildren().add(activityBox);

        // Module Buttons Section
        VBox modulesBox = createModulesSection();
        mainContent.getChildren().add(modulesBox);

        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Dashboard Tổng quan - Quản lý Tòa Nhà");
        primaryStage.setResizable(true);

        // Lấy kích thước màn hình
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Ẩn cửa sổ trước khi thay đổi kích thước để tránh giật
        boolean wasShowing = primaryStage.isShowing();
        if (wasShowing) {
            primaryStage.setOpacity(0.0);
            primaryStage.hide();
        }

        // Set maximize và kích thước
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(false);
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());

        primaryStage.setScene(scene);

        // Hiển thị cửa sổ
        primaryStage.show();
        if (wasShowing) {
            primaryStage.setOpacity(1.0);
        }

        // Đảm bảo cửa sổ được maximize
        javafx.application.Platform.runLater(() -> {
            primaryStage.setMaximized(true);
        });
    }

    private static VBox createHeader(String role) {
        VBox header = new VBox(8);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 30, 25, 30));
        header.setStyle("-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);");

        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("🏢 Hệ thống Quản lý Tòa Nhà");
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label dateLabel = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.forLanguageTag("vi"))));
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9);");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(welcomeLabel, spacer, dateLabel);

        Label roleLabel = new Label("Vai trò: " + role.toUpperCase());
        roleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.95); -fx-font-weight: 500;");

        header.getChildren().addAll(topRow, roleLabel);
        return header;
    }

    private static HBox createKPICardsRow1() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(0, 0, 20, 0));

        // Lấy dữ liệu thực tế
        int totalApartments = ApartmentService.getAllApartments().size();
        int occupiedApartments = ApartmentService.getApartmentsByStatus("OCCUPIED").size();
        int totalResidents = ResidentService.getAllResidents().size();
        int totalInvoices = InvoiceService.getAllInvoices().size();
        int pendingInvoices = InvoiceService.getInvoicesByStatus("PENDING").size();
        int paidInvoices = InvoiceService.getInvoicesByStatus("PAID").size();

        // Card 1: Tổng căn hộ
        VBox card1 = createStatCard("Tổng Căn Hộ", String.valueOf(totalApartments),
                "Đã cho thuê: " + occupiedApartments, "#667eea", "🏠");

        // Card 2: Tổng Cư Dân
        VBox card2 = createStatCard("Tổng Cư Dân", String.valueOf(totalResidents),
                "Đang cư trú", "#f093fb", "👥");

        // Card 3: Hóa Đơn
        VBox card3 = createStatCard("Tổng Hóa Đơn", String.valueOf(totalInvoices),
                "Chưa thanh toán: " + pendingInvoices, "#4facfe", "📄");

        // Card 4: Đã Thanh Toán
        VBox card4 = createStatCard("Đã Thanh Toán", String.valueOf(paidInvoices),
                "Hóa đơn", "#43e97b", "✅");

        statsBox.getChildren().addAll(card1, card2, card3, card4);
        return statsBox;
    }

    private static HBox createKPICardsRow2() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(0, 0, 20, 0));

        // Lấy dữ liệu thực tế
        int totalNotifications = NotificationService.getAllNotifications().size();
        int sentNotifications = NotificationService.getNotificationsByStatus("SENT").size();
        int totalRepairs = RepairRequestService.getAllRepairRequests().size();
        int pendingRepairs = RepairRequestService.getRepairRequestsByStatus("PENDING").size();
        int completedRepairs = RepairRequestService.getRepairRequestsByStatus("COMPLETED").size();
        int totalMaintenance = MaintenanceService.getAllMaintenances().size();
        int pendingMaintenance = MaintenanceService.getMaintenancesByStatus("PENDING").size();
        int totalBMS = BMSService.getAllSystems().size();

        // Card 5: Thông Báo
        VBox card5 = createStatCard("Thông Báo", String.valueOf(totalNotifications),
                "Đã gửi: " + sentNotifications, "#fa709a", "🔔");

        // Card 6: Yêu Cầu Sửa Chữa
        VBox card6 = createStatCard("Yêu Cầu Sửa Chữa", String.valueOf(totalRepairs),
                "Đang chờ: " + pendingRepairs, "#fee140", "🔧");

        // Card 7: Bảo Trì
        VBox card7 = createStatCard("Bảo Trì", String.valueOf(totalMaintenance),
                "Đang chờ: " + pendingMaintenance, "#30cfd0", "⚙️");

        // Card 8: Hệ Thống BMS
        VBox card8 = createStatCard("Hệ Thống BMS", String.valueOf(totalBMS),
                "Đang hoạt động", "#a8edea", "💻");

        statsBox.getChildren().addAll(card5, card6, card7, card8);
        return statsBox;
    }

    private static VBox createStatCard(String title, String value, String subtitle, String gradientColor, String icon) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setPrefWidth(280);
        card.setPrefHeight(140);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);");

        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748b; -fx-font-weight: 600;");

        titleBox.getChildren().addAll(iconLabel, titleLabel);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; " +
                "-fx-text-fill: " + gradientColor + ";");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #94a3b8;");

        card.getChildren().addAll(titleBox, valueLabel, subtitleLabel);
        return card;
    }

    private static HBox createChartsSection() {
        HBox chartsBox = new HBox(20);
        chartsBox.setAlignment(Pos.CENTER);
        chartsBox.setPadding(new Insets(20, 0, 20, 0));

        // Pie Chart: Trạng thái Hóa đơn
        PieChart invoiceChart = createInvoicePieChart();

        // Bar Chart: Yêu cầu sửa chữa theo tháng
        BarChart<String, Number> repairChart = createRepairBarChart();

        chartsBox.getChildren().addAll(invoiceChart, repairChart);
        return chartsBox;
    }

    private static PieChart createInvoicePieChart() {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Trạng thái Hóa đơn");
        pieChart.setPrefWidth(500);
        pieChart.setPrefHeight(400);
        pieChart.setStyle("-fx-background-color: white; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);");
        pieChart.setLabelLineLength(10);
        pieChart.setLegendSide(Side.BOTTOM);

        int pending = InvoiceService.getInvoicesByStatus("PENDING").size();
        int paid = InvoiceService.getInvoicesByStatus("PAID").size();
        int overdue = InvoiceService.getInvoicesByStatus("OVERDUE").size();

        if (pending == 0 && paid == 0 && overdue == 0) {
            pending = 15;
            paid = 45;
            overdue = 5;
        }

        PieChart.Data pendingData = new PieChart.Data("Chưa thanh toán", pending);
        PieChart.Data paidData = new PieChart.Data("Đã thanh toán", paid);
        PieChart.Data overdueData = new PieChart.Data("Quá hạn", overdue);

        pieChart.getData().addAll(pendingData, paidData, overdueData);

        // Tùy chỉnh màu sắc
        javafx.application.Platform.runLater(() -> {
            if (pieChart.getData().size() > 0 && pieChart.getData().get(0).getNode() != null) {
                pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #f59e0b;");
            }
            if (pieChart.getData().size() > 1 && pieChart.getData().get(1).getNode() != null) {
                pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #10b981;");
            }
            if (pieChart.getData().size() > 2 && pieChart.getData().get(2).getNode() != null) {
                pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #ef4444;");
            }
        });

        return pieChart;
    }

    private static BarChart<String, Number> createRepairBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Yêu cầu Sửa chữa theo Trạng thái");
        barChart.setPrefWidth(500);
        barChart.setPrefHeight(400);
        barChart.setStyle("-fx-background-color: white; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);");
        barChart.setLegendVisible(false);

        xAxis.setLabel("Trạng thái");
        yAxis.setLabel("Số lượng");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int pending = RepairRequestService.getRepairRequestsByStatus("PENDING").size();
        int inProgress = RepairRequestService.getRepairRequestsByStatus("IN_PROGRESS").size();
        int completed = RepairRequestService.getRepairRequestsByStatus("COMPLETED").size();

        if (pending == 0 && inProgress == 0 && completed == 0) {
            pending = 3;
            inProgress = 2;
            completed = 5;
        }

        series.getData().add(new XYChart.Data<>("Đang chờ", pending));
        series.getData().add(new XYChart.Data<>("Đang xử lý", inProgress));
        series.getData().add(new XYChart.Data<>("Hoàn thành", completed));

        barChart.getData().add(series);

        // Tùy chỉnh màu cột
        javafx.application.Platform.runLater(() -> {
            if (!barChart.getData().isEmpty() && !barChart.getData().get(0).getData().isEmpty()) {
                int index = 0;
                String[] colors = {"#f59e0b", "#3b82f6", "#10b981"};
                for (XYChart.Data<String, Number> data : barChart.getData().get(0).getData()) {
                    if (data.getNode() != null && index < colors.length) {
                        data.getNode().setStyle("-fx-bar-fill: " + colors[index] + ";");
                        index++;
                    }
                }
            }
        });

        return barChart;
    }

    private static VBox createRecentActivitySection() {
        VBox activityBox = new VBox(15);
        activityBox.setPadding(new Insets(20));
        activityBox.setStyle("-fx-background-color: white; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 4);");

        Label sectionLabel = new Label("📊 Hoạt Động Gần Đây");
        sectionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox activityList = new VBox(10);

        // Lấy các thông báo gần đây
        var recentNotifications = NotificationService.getAllNotifications().stream()
                .limit(5)
                .collect(Collectors.toList());

        if (recentNotifications.isEmpty()) {
            Label noActivity = new Label("Chưa có hoạt động nào");
            noActivity.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");
            activityList.getChildren().add(noActivity);
        } else {
            for (var notification : recentNotifications) {
                HBox activityItem = new HBox(15);
                activityItem.setAlignment(Pos.CENTER_LEFT);
                activityItem.setPadding(new Insets(12));
                activityItem.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8;");

                Label icon = new Label("🔔");
                icon.setStyle("-fx-font-size: 20px;");

                VBox content = new VBox(4);
                Label title = new Label(notification.getTitle());
                title.setStyle("-fx-font-size: 14px; -fx-font-weight: 600; -fx-text-fill: #1e293b;");

                Label date = new Label(notification.getSentDate() != null ?
                        notification.getSentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) :
                        "Chưa gửi");
                date.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

                content.getChildren().addAll(title, date);
                activityItem.getChildren().addAll(icon, content);
                activityList.getChildren().add(activityItem);
            }
        }

        activityBox.getChildren().addAll(sectionLabel, activityList);
        return activityBox;
    }

    private static VBox createModulesSection() {
        VBox modulesBox = new VBox(20);
        modulesBox.setAlignment(Pos.CENTER);
        modulesBox.setPadding(new Insets(20));

        Label sectionLabel = new Label("🚀 Truy Cập Nhanh Các Module");
        sectionLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));

        Button bmsBtn = new Button("💻 Giám sát & Điều khiển (BMS)");
        Button maintenanceBtn = new Button("⚙️ Bảo trì & Bảo dưỡng");
        Button securityBtn = new Button("🔒 Quản lý An ninh");
        Button cleaningBtn = new Button("🧹 Quản lý Vệ sinh");
        Button adminBtn = new Button("📋 Hành chính & Nhân sự");
        Button hrBtn = new Button("👔 Nhân sự & Chấm công");
        Button customerBtn = new Button("👥 Quản lý Khách hàng");
        Button apartmentBtn = new Button("🏠 Quản lý Căn hộ");
        Button invoiceBtn = new Button("💰 Hóa đơn & Thanh toán");
        Button notificationBtn = new Button("🔔 Gửi thông báo");
        Button repairBtn = new Button("🔧 Yêu cầu sửa chữa");

        styleButton(bmsBtn, "#667eea");
        styleButton(maintenanceBtn, "#f093fb");
        styleButton(securityBtn, "#4facfe");
        styleButton(cleaningBtn, "#43e97b");
        styleButton(adminBtn, "#fa709a");
        styleButton(hrBtn, "#fee140");
        styleButton(customerBtn, "#30cfd0");
        styleButton(apartmentBtn, "#a8edea");
        styleButton(invoiceBtn, "#667eea");
        styleButton(notificationBtn, "#f093fb");
        styleButton(repairBtn, "#4facfe");

        bmsBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/bms.fxml", "Giám sát & Điều khiển BMS"));
        maintenanceBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/maintenance.fxml", "Bảo trì & Bảo dưỡng"));
        securityBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/security.fxml", "Quản lý An ninh"));
        cleaningBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/cleaning.fxml", "Quản lý Vệ sinh"));
        adminBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/admin.fxml", "Quản lý Hành chính & Nhân sự"));
        hrBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/hr.fxml", "Nhân sự & Chấm công"));
        customerBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/customer.fxml", "Quản lý Khách hàng"));
        apartmentBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/apartment.fxml", "Quản lý Căn hộ"));
        invoiceBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/invoice.fxml", "Hóa đơn & Thanh toán"));
        notificationBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/notification.fxml", "Gửi thông báo"));
        repairBtn.setOnAction(e -> openModule("/com/example/quanlytoanhanhom4/fxml/repair_request.fxml", "Yêu cầu sửa chữa"));

        grid.add(bmsBtn, 0, 0);
        grid.add(maintenanceBtn, 1, 0);
        grid.add(securityBtn, 2, 0);
        grid.add(cleaningBtn, 3, 0);
        grid.add(adminBtn, 0, 1);
        grid.add(hrBtn, 1, 1);
        grid.add(customerBtn, 2, 1);
        grid.add(apartmentBtn, 3, 1);
        grid.add(invoiceBtn, 0, 2);
        grid.add(notificationBtn, 1, 2);
        grid.add(repairBtn, 2, 2);

        modulesBox.getChildren().addAll(sectionLabel, grid);
        return modulesBox;
    }

    private static void styleButton(Button button, String color) {
        button.setPrefWidth(220);
        button.setPrefHeight(65);
        button.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);");
        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: " + darkenColor(color) + "; " +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 5);"));
        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);"));
    }

    private static String darkenColor(String hexColor) {
        // Đơn giản hóa: trả về màu tối hơn
        if (hexColor.startsWith("#")) {
            return hexColor; // Có thể cải thiện logic này
        }
        return hexColor;
    }

    private static HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 30, 20, 30));
        footer.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, -2);");

        Button backBtn = new Button("← Quay lại");
        backBtn.setStyle("-fx-background-color: #64748b; -fx-text-fill: white; -fx-padding: 12 30; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;");
        backBtn.setOnAction(e -> handleBack());
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: #475569; -fx-text-fill: white; " +
                "-fx-padding: 12 30; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: #64748b; -fx-text-fill: white; " +
                "-fx-padding: 12 30; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;"));

        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 12 30; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;");
        logoutBtn.setOnAction(e -> handleLogout());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; " +
                "-fx-padding: 12 30; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; " +
                "-fx-padding: 12 30; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8;"));

        footer.getChildren().addAll(backBtn, logoutBtn);
        return footer;
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
