package com.example.quanlytoanhanhom4.controller;

import com.example.quanlytoanhanhom4.ui.BuildingLogo;
import com.example.quanlytoanhanhom4.ui.DashboardView;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    
    @FXML
    private HBox topBar;
    
    @FXML
    private Label adminLabel;
    
    @FXML
    private Button dashboardBtn;
    
    @FXML
    private Button dienBtn;
    
    @FXML
    private Button pcccBtn;
    
    @FXML
    private Button chieuSangBtn;
    
    @FXML
    private Button baoTriBtn;

    @FXML
    private Button hrBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lấy thông tin từ UserSession
        String username = UserSession.getCurrentUsername();
        String role = UserSession.getCurrentRole();
        
        // Cập nhật label chào mừng
        if (adminLabel != null) {
            adminLabel.setText("CHÀO " + (username != null ? username.toUpperCase() : "ADMIN"));
        }

        // Thêm logo vào đầu topBar
        if (topBar != null) {
            BuildingLogo logo = new BuildingLogo(60, 60);
            StackPane logoContainer = new StackPane(logo);
            logoContainer.setPadding(new Insets(0, 10, 0, 0));
            logoContainer.setMaxWidth(70); // Giới hạn kích thước để không vỡ giao diện
            logoContainer.setMaxHeight(70);
            topBar.getChildren().add(0, logoContainer);
        }
    }
    
    private void openDashboard() {
        try {
            Stage currentStage = (Stage) topBar.getScene().getWindow();
            String role = UserSession.getCurrentRole();

            // Đảm bảo cửa sổ được maximize và resize
            currentStage.setResizable(true);
            
            DashboardView.show(currentStage, role != null ? role : "user");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openModule(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage moduleStage = new Stage();
            Scene scene = new Scene(loader.load());
            
            moduleStage.setTitle(title);
            moduleStage.setScene(scene);

            // Cho phép resize và maximize
            moduleStage.setResizable(true);

            // Maximize cửa sổ để hiển thị toàn màn hình
            moduleStage.setMaximized(true);
            
            moduleStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleDashboard() {
        openDashboard();
    }
    
    @FXML
    private void handleBms() {
        openModule("/com/example/quanlytoanhanhom4/fxml/bms.fxml", "Giám sát & Điều khiển (BMS)");
    }
    
    @FXML
    private void handlePccc() {
        openModule("/com/example/quanlytoanhanhom4/fxml/bms.fxml", "Phòng cháy chữa cháy");
    }
    
    @FXML
    private void handleLighting() {
        openModule("/com/example/quanlytoanhanhom4/fxml/bms.fxml", "Chiếu sáng & Tiện ích");
    }
    
    @FXML
    private void handleMaintenance() {
        openModule("/com/example/quanlytoanhanhom4/fxml/maintenance.fxml", "Quản lý Bảo trì");
    }
    
    @FXML
    private void handleSecurity() {
        openModule("/com/example/quanlytoanhanhom4/fxml/security.fxml", "Quản lý An ninh");
    }
    
    @FXML
    private void handleCleaning() {
        openModule("/com/example/quanlytoanhanhom4/fxml/cleaning.fxml", "Quản lý Vệ sinh");
    }
    
    @FXML
    private void handleCustomer() {
        openModule("/com/example/quanlytoanhanhom4/fxml/customer.fxml", "Quản lý Khách hàng");
    }
    
    @FXML
    private void handleAdmin() {
        openModule("/com/example/quanlytoanhanhom4/fxml/admin.fxml", "Quản lý Hành chính & Nhân sự");
    }

    @FXML
    private void handleHR() {
        openModule("/com/example/quanlytoanhanhom4/fxml/hr.fxml", "Quản lý Nhân sự & Chấm công");
    }

    @FXML
    private void handleResident() {
        openModule("/com/example/quanlytoanhanhom4/fxml/resident.fxml", "Quản lý Cư dân");
    }

    @FXML
    private void handleApartment() {
        openModule("/com/example/quanlytoanhanhom4/fxml/apartment.fxml", "Quản lý Căn hộ");
    }

    @FXML
    private void handleUtility() {
        openModule("/com/example/quanlytoanhanhom4/fxml/utility.fxml", "Quản lý Điện - Nước - Phí dịch vụ");
    }

    @FXML
    private void handleInvoice() {
        openModule("/com/example/quanlytoanhanhom4/fxml/invoice.fxml", "Hóa đơn & Thanh toán");
    }

    @FXML
    private void handleNotification() {
        openModule("/com/example/quanlytoanhanhom4/fxml/notification.fxml", "Gửi thông báo");
    }

    @FXML
    private void handleRepairRequest() {
        openModule("/com/example/quanlytoanhanhom4/fxml/repair_request.fxml", "Quản lý Yêu cầu Sửa chữa");
    }

    @FXML
    private void handleLogout() {
        try {
            Stage currentStage = (Stage) topBar.getScene().getWindow();
            currentStage.close();
            
            // Xóa thông tin session
            UserSession.clear();
            
            // Mở lại màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlytoanhanhom4/fxml/login.fxml"));
            Stage loginStage = new Stage();
            Scene scene = new Scene(loader.load(), 400, 350);
            loginStage.setTitle("Đăng nhập quản lý toà nhà");
            loginStage.setResizable(false);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

