package com.example.quanlytoanhanhom4.controller.auth;

import com.example.quanlytoanhanhom4.service.auth.UserService;
import com.example.quanlytoanhanhom4.ui.BuildingLogo;
import com.example.quanlytoanhanhom4.ui.auth.RegisterForm;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import com.example.quanlytoanhanhom4.util.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox loginVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Thêm logo vào đầu VBox
        if (loginVBox != null) {
            BuildingLogo logo = new BuildingLogo(60, 60);
            StackPane logoContainer = new StackPane(logo);
            logoContainer.setPadding(new Insets(0, 0, 10, 0));
            logoContainer.setMaxWidth(70); // Giới hạn kích thước để không vỡ giao diện
            logoContainer.setMaxHeight(70);
            loginVBox.getChildren().add(0, logoContainer);
        }
    }

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtils.showWarning("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            String role = UserService.verifyLogin(username, password);
            if (role != null) {
                logger.info("Đăng nhập thành công cho user: {}", username);
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("✅ Đăng nhập thành công!");
                openMainView();
            } else {
                logger.warn("Đăng nhập thất bại cho user: {}", username);
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("❌ Sai tên đăng nhập hoặc mật khẩu!");
                AlertUtils.showError("Đăng nhập thất bại", "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (Exception e) {
            logger.error("Lỗi khi đăng nhập", e);
            AlertUtils.showError("Lỗi", "Đã xảy ra lỗi khi đăng nhập. Vui lòng thử lại.");
        }
    }

    @FXML
    protected void handleRegister() {
        try {
            RegisterForm registerForm = new RegisterForm();
            registerForm.start(new Stage());
            logger.debug("Mở màn hình đăng ký");
        } catch (Exception e) {
            logger.error("Lỗi khi mở màn hình đăng ký", e);
            AlertUtils.showError("Lỗi", "Không thể mở màn hình đăng ký: " + e.getMessage());
        }
    }

    private void openMainView() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlytoanhanhom4/fxml/main.fxml"));
            Scene scene = new Scene(loader.load(), 1080, 640);
            currentStage.setTitle("Quản lý kỹ thuật tòa nhà");
            currentStage.setScene(scene);
            currentStage.setResizable(true);
            currentStage.setMaximized(true);
            currentStage.show();
            logger.debug("Đã mở giao diện chính");
        } catch (Exception e) {
            logger.error("Lỗi khi tải giao diện chính", e);
            AlertUtils.showError("Lỗi", "Không thể tải giao diện chính: " + e.getMessage());
            UserSession.clear();
        }
    }
}


