package com.example.quanlytoanhanhom4.controller.auth;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.util.PasswordUtils;
import com.example.quanlytoanhanhom4.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    // Phone regex pattern (Vietnam: 10 digits, starting with 0)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");

    // Username regex pattern (alphanumeric, underscore, dot, 3-20 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._]{3,20}$");
    
    // Map để chuyển đổi vai trò từ tiếng Việt sang tiếng Anh
    private static final Map<String, String> ROLE_MAP = new HashMap<>();
    
    // Admin secret key
    private static final String ADMIN_SECRET_KEY = "BMS-ADMIN-2025-SECURITY";
    
    static {
        ROLE_MAP.put("Quản trị viên", "ADMIN");
        ROLE_MAP.put("Người dùng", "RESIDENT");
    }

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField fullNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private ComboBox<String> roleComboBox;
    
    @FXML
    private Label usernameError;
    
    @FXML
    private Label passwordError;
    
    @FXML
    private Label confirmPasswordError;
    
    @FXML
    private Label fullNameError;
    
    @FXML
    private Label emailError;
    
    @FXML
    private Label phoneError;
    
    @FXML
    private Label roleError;
    
    @FXML
    private PasswordField secretKeyField;
    
    @FXML
    private Label secretKeyError;
    
    @FXML
    private VBox secretKeyContainer;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label loginLinkLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup role combo box với tiếng Việt
        roleComboBox.getItems().addAll(
            "Người dùng", "Quản trị viên"
        );
        // Đặt giá trị mặc định
        roleComboBox.setValue("Người dùng");

        // Setup real-time validation listeners
        setupValidationListeners();

        // Setup login link click handler
        loginLinkLabel.setOnMouseClicked(e -> handleCancel());
        loginLinkLabel.getStyleClass().add("login-link-label");
    }

    private void setupValidationListeners() {
        // Username validation
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateUsername();
        });

        // Password validation
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePassword();
            if (!confirmPasswordField.getText().isEmpty()) {
                validateConfirmPassword();
            }
        });

        // Confirm password validation
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateConfirmPassword();
        });

        // Full name validation
        fullNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateFullName();
        });

        // Email validation
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateEmail();
        });

        // Phone validation
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePhone();
        });

        // Role validation
        roleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            validateRole();
            // Hiển thị/ẩn trường secret key khi chọn role Admin
            if ("Quản trị viên".equals(newVal)) {
                secretKeyContainer.setVisible(true);
                secretKeyContainer.setManaged(true);
            } else {
                secretKeyContainer.setVisible(false);
                secretKeyContainer.setManaged(false);
                secretKeyField.clear();
                secretKeyError.setVisible(false);
            }
        });

        // Secret key validation
        secretKeyField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (secretKeyContainer.isVisible()) {
                validateSecretKey();
            }
        });
    }

    @FXML
    private void handleRegister() {
        // Clear previous status
        statusLabel.setVisible(false);
        
        // Validate all fields
        boolean isValid = true;
        
        if (!validateUsername()) isValid = false;
        if (!validatePassword()) isValid = false;
        if (!validateConfirmPassword()) isValid = false;
        if (!validateFullName()) isValid = false;
        if (!validateEmail()) isValid = false;
        if (!validatePhone()) isValid = false;
        if (!validateRole()) isValid = false;
        
        // Validate secret key nếu chọn role Admin
        if ("Quản trị viên".equals(roleComboBox.getValue())) {
            if (!validateSecretKey()) isValid = false;
        }

        if (!isValid) {
            showError("Vui lòng sửa các lỗi trước khi đăng ký!");
            return;
        }

        // Check if username already exists
        if (checkUsernameExists(usernameField.getText().trim())) {
            showError("Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
            usernameError.setText("Tên đăng nhập đã tồn tại!");
            usernameError.setVisible(true);
            usernameField.requestFocus();
            return;
        }

        // Check if email already exists
        if (checkEmailExists(emailField.getText().trim())) {
            showError("Email đã được sử dụng! Vui lòng chọn email khác.");
            emailError.setText("Email đã được sử dụng!");
            emailError.setVisible(true);
            emailField.requestFocus();
            return;
        }

        // Register user
        if (registerUser()) {
            showSuccess("Đăng ký thành công! Vui lòng đăng nhập.");
            logger.info("Đăng ký thành công cho user: {}", usernameField.getText().trim());
            
            // Close window after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.close();
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } else {
            showError("Đăng ký thất bại! Vui lòng thử lại.");
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private boolean validateUsername() {
        String username = usernameField.getText().trim();
        usernameError.setVisible(false);

        if (username.isEmpty()) {
            usernameError.setText("Tên đăng nhập không được để trống!");
            usernameError.setVisible(true);
            return false;
        }

        if (username.length() < 3) {
            usernameError.setText("Tên đăng nhập phải có ít nhất 3 ký tự!");
            usernameError.setVisible(true);
            return false;
        }

        if (username.length() > 20) {
            usernameError.setText("Tên đăng nhập không được vượt quá 20 ký tự!");
            usernameError.setVisible(true);
            return false;
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            usernameError.setText("Tên đăng nhập chỉ được chứa chữ, số, dấu chấm và dấu gạch dưới!");
            usernameError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        String password = passwordField.getText();
        passwordError.setVisible(false);

        if (password.isEmpty()) {
            passwordError.setText("Mật khẩu không được để trống!");
            passwordError.setVisible(true);
            return false;
        }

        if (password.length() < 6) {
            passwordError.setText("Mật khẩu phải có ít nhất 6 ký tự!");
            passwordError.setVisible(true);
            return false;
        }

        if (password.length() > 50) {
            passwordError.setText("Mật khẩu không được vượt quá 50 ký tự!");
            passwordError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        confirmPasswordError.setVisible(false);

        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText("Vui lòng xác nhận mật khẩu!");
            confirmPasswordError.setVisible(true);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Mật khẩu xác nhận không khớp!");
            confirmPasswordError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validateFullName() {
        String fullName = fullNameField.getText().trim();
        fullNameError.setVisible(false);

        if (fullName.isEmpty()) {
            fullNameError.setText("Họ và tên không được để trống!");
            fullNameError.setVisible(true);
            return false;
        }

        if (fullName.length() < 2) {
            fullNameError.setText("Họ và tên phải có ít nhất 2 ký tự!");
            fullNameError.setVisible(true);
            return false;
        }

        if (fullName.length() > 150) {
            fullNameError.setText("Họ và tên không được vượt quá 150 ký tự!");
            fullNameError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        String email = emailField.getText().trim();
        emailError.setVisible(false);

        if (email.isEmpty()) {
            emailError.setText("Email không được để trống!");
            emailError.setVisible(true);
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            emailError.setText("Email không đúng định dạng! (VD: example@email.com)");
            emailError.setVisible(true);
            return false;
        }

        if (email.length() > 100) {
            emailError.setText("Email không được vượt quá 100 ký tự!");
            emailError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validatePhone() {
        String phone = phoneField.getText().trim();
        phoneError.setVisible(false);

        if (phone.isEmpty()) {
            phoneError.setText("Số điện thoại không được để trống!");
            phoneError.setVisible(true);
            return false;
        }

        // Remove spaces and dashes for validation
        String cleanedPhone = phone.replaceAll("[\\s-]", "");

        if (!PHONE_PATTERN.matcher(cleanedPhone).matches()) {
            phoneError.setText("Số điện thoại không đúng định dạng! (VD: 0987654321)");
            phoneError.setVisible(true);
            return false;
        }

        // Update field with cleaned phone
        if (!phone.equals(cleanedPhone)) {
            phoneField.setText(cleanedPhone);
        }

        return true;
    }

    private boolean validateRole() {
        roleError.setVisible(false);

        if (roleComboBox.getValue() == null || roleComboBox.getValue().isEmpty()) {
            roleError.setText("Vui lòng chọn vai trò!");
            roleError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean validateSecretKey() {
        String secretKey = secretKeyField.getText();
        secretKeyError.setVisible(false);

        if (secretKey.isEmpty()) {
            secretKeyError.setText("Mã bảo mật Admin không được để trống!");
            secretKeyError.setVisible(true);
            return false;
        }

        if (!ADMIN_SECRET_KEY.equals(secretKey)) {
            secretKeyError.setText("Mã bảo mật Admin không đúng!");
            secretKeyError.setVisible(true);
            return false;
        }

        return true;
    }

    private boolean checkUsernameExists(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM user WHERE username = ?")) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi kiểm tra username tồn tại: {}", username, e);
            return false;
        }
    }

    private boolean checkEmailExists(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM user WHERE email = ?")) {
            
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi kiểm tra email tồn tại: {}", email, e);
            return false;
        }
    }

    private boolean registerUser() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO user (username, password, role, full_name, phone, email, is_active) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            // Chuyển đổi vai trò từ tiếng Việt sang tiếng Anh
            String roleVietnamese = roleComboBox.getValue();
            String roleEnglish = ROLE_MAP.getOrDefault(roleVietnamese, "RESIDENT");
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, usernameField.getText().trim());
                stmt.setString(2, PasswordUtils.hashPassword(passwordField.getText()));
                stmt.setString(3, roleEnglish);
                stmt.setString(4, fullNameField.getText().trim());
                stmt.setString(5, phoneField.getText().trim());
                stmt.setString(6, emailField.getText().trim());
                stmt.setBoolean(7, true);

                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi đăng ký user: {}", usernameField.getText().trim(), e);
            return false;
        }
    }

    private void showError(String message) {
        statusLabel.getStyleClass().removeAll("success", "error");
        statusLabel.getStyleClass().add("error");
        statusLabel.setText("❌ " + message);
        statusLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        statusLabel.getStyleClass().removeAll("success", "error");
        statusLabel.getStyleClass().add("success");
        statusLabel.setText("✅ " + message);
        statusLabel.setVisible(true);
    }
}

