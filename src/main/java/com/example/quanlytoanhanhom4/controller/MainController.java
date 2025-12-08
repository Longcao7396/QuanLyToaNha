package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.service.ClockService;
import com.example.quanlytoanhanhom4.ui.BuildingLogo;
import com.example.quanlytoanhanhom4.ui.DashboardCharts;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    @FXML
    private HBox topBar;
    
    @FXML
    private Label adminLabel;
    
    @FXML
    private VBox mainContentVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lấy thông tin từ UserSession
        String username = UserSession.getCurrentUsername();
        String role = UserSession.getCurrentRole();
        
        // Cập nhật label chào mừng
        if (adminLabel != null) {
            adminLabel.setText("CHÀO " + (username != null ? username.toUpperCase() : "ADMIN"));
        }

        // Thêm logo vào đầu topBar - dùng màu cho nền xanh
        if (topBar != null) {
            BuildingLogo logo = new BuildingLogo(60, 60, true); // true = cho nền xanh
            StackPane logoContainer = new StackPane(logo);
            logoContainer.setPadding(new Insets(0, 10, 0, 0));
            logoContainer.setMaxWidth(70); // Giới hạn kích thước để không vỡ giao diện
            logoContainer.setMaxHeight(70);
            topBar.getChildren().add(0, logoContainer);
            
            // Thêm thông tin thời tiết và giờ vào topBar
            addWeatherAndClockToTopBar();
        }
        
        // Thêm biểu đồ dashboard vào màn hình chính
        if (mainContentVBox != null) {
            DashboardCharts dashboardCharts = new DashboardCharts();
            mainContentVBox.getChildren().add(dashboardCharts);
        }
    }
    
    /**
     * Thêm thông tin thời tiết và đồng hồ vào topBar
     * Sử dụng WeatherWidget component mới - đồng nhất và gọn gàng
     */
    private void addWeatherAndClockToTopBar() {
        if (topBar == null) {
            logger.warn("topBar is null, không thể thêm thời tiết");
            return;
        }
        
        // Tạo HBox chứa thời tiết và giờ
        HBox infoBox = new HBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle("-fx-padding: 0 10 0 0;");
        
        // Tạo đồng hồ trước (luôn hiển thị)
        Label clockLabel = ClockService.createClockLabel(false);
        clockLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        infoBox.getChildren().add(clockLabel);
        
        // Sử dụng WeatherWidget component mới - compact mode cho top bar
        HBox weatherWidget = com.example.quanlytoanhanhom4.ui.WeatherWidget.create(true);
        infoBox.getChildren().add(0, weatherWidget);
        
        // Thêm vào topBar
        int lastIndex = topBar.getChildren().size() - 1;
        if (lastIndex >= 0) {
            topBar.getChildren().add(lastIndex, infoBox);
        } else {
            topBar.getChildren().add(infoBox);
        }
    }
    
    private void openModule(String fxmlPath, String title) {
        try {
            java.net.URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                logger.error("Không tìm thấy file FXML: {}", fxmlPath);
                AlertUtils.showError(
                        "Lỗi",
                        "Không tìm thấy file: " + fxmlPath + "\nVui lòng kiểm tra lại đường dẫn."
                );
                return;
            }

            logger.debug("Đang mở module: {} từ {}", title, fxmlPath);
            FXMLLoader loader = new FXMLLoader(resource);
            Stage moduleStage = new Stage();
            Scene scene = new Scene(loader.load());

            moduleStage.setTitle(title);
            moduleStage.setScene(scene);

            // Cho phép resize và maximize
            moduleStage.setResizable(true);

            // Maximize cửa sổ để hiển thị toàn màn hình
            moduleStage.setMaximized(true);
            
            moduleStage.show();
            logger.info("Đã mở cửa sổ: {}", title);
        } catch (Exception e) {
            logger.error("Lỗi khi mở cửa sổ: {}", title, e);
            AlertUtils.showError(
                    "Lỗi",
                    "Không thể mở cửa sổ: " + title + "\n" + e.getMessage()
            );
        }
    }
    
    // ======================
    // MODULE 1: QUẢN LÝ CƯ DÂN & CĂN HỘ
    // ======================
    @FXML
    private void handleResident() {
        openModule("/com/example/quanlytoanhanhom4/fxml/resident.fxml", "Quản lý Cư dân");
    }

    @FXML
    private void handleApartment() {
        openModule("/com/example/quanlytoanhanhom4/fxml/apartment.fxml", "Quản lý Căn hộ");
    }

    // ======================
    // MODULE 2 & 3: QUẢN LÝ PHÍ & CÔNG NỢ + ĐIỆN - NƯỚC
    // ======================
    @FXML
    private void handleServiceFee() {
        openModule("/com/example/quanlytoanhanhom4/fxml/service_fee.fxml", "Quản lý Phí Dịch vụ & Điện Nước");
    }

    @FXML
    private void handleInvoice() {
        openModule("/com/example/quanlytoanhanhom4/fxml/invoice.fxml", "Quản lý Hóa đơn & Thanh toán");
    }

    // ======================
    // MODULE 4: QUẢN LÝ YÊU CẦU CƯ DÂN (TICKET)
    // ======================
    @FXML
    private void handleTicket() {
        openModule("/com/example/quanlytoanhanhom4/fxml/ticket.fxml", "Quản lý Yêu cầu & Sự cố");
    }

    // ======================
    // MODULE 5: GỬI THÔNG BÁO CHO CƯ DÂN
    // ======================
    @FXML
    private void handleNotification() {
        openModule("/com/example/quanlytoanhanhom4/fxml/notification.fxml", "Quản lý Thông báo");
    }

    @FXML
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) topBar.getScene().getWindow();
            currentStage.close();
            
            // Xóa thông tin session
            UserSession.clear();
            logger.info("User đã đăng xuất");
            
            // Mở lại màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlytoanhanhom4/fxml/login.fxml"));
            Stage loginStage = new Stage();
            Scene scene = new Scene(loader.load(), 400, 350);
            loginStage.setTitle("Đăng nhập quản lý toà nhà");
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();
            logger.debug("Đã mở lại màn hình đăng nhập");
        } catch (Exception e) {
            logger.error("Lỗi khi đăng xuất", e);
            AlertUtils.showError("Lỗi", "Không thể đăng xuất: " + e.getMessage());
        }
    }
}

