package com.building.management.controller;

import com.building.management.navigation.NavigationManager;
import com.building.management.service.UserService;
import com.building.management.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Login Controller
 * Handles login screen UI interactions only
 * Business logic is in UserService
 */
public class LoginController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty()) {
            AlertUtils.showError("Lỗi", "Vui lòng nhập tên đăng nhập");
            usernameField.requestFocus();
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            AlertUtils.showError("Lỗi", "Vui lòng nhập mật khẩu");
            passwordField.requestFocus();
            return;
        }

        // Call service layer for authentication
        var user = UserService.authenticate(username, password);

        if (user != null) {
            logger.info("Login successful for user: {}", username);
            NavigationManager.switchToDashboard();
        } else {
            AlertUtils.showError("Đăng nhập thất bại", "Tên đăng nhập hoặc mật khẩu không đúng");
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}


