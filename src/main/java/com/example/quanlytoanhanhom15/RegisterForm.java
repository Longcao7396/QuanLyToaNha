package com.example.quanlytoanhanhom15;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class RegisterForm extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Đăng ký tài khoản");

        Label userLabel = new Label("Tên đăng nhập:");
        TextField userField = new TextField();

        Label passLabel = new Label("Mật khẩu:");
        PasswordField passField = new PasswordField();

        Label roleLabel = new Label("Vai trò:");
        TextField roleField = new TextField();
        roleField.setPromptText("VD: admin, user...");

        Label phoneLabel = new Label("Số điện thoại:");
        TextField phoneField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Button registerButton = new Button("Đăng ký");
        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String role = roleField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();

            if (registerUser(username, password, role, phone, email)) {
                messageLabel.setText("✅ Đăng ký thành công!");
            } else {
                messageLabel.setText("❌ Lỗi: Tên người dùng đã tồn tại hoặc lỗi hệ thống!");
            }
        });

        VBox layout = new VBox(10,
                userLabel, userField,
                passLabel, passField,
                roleLabel, roleField,
                phoneLabel, phoneField,
                emailLabel, emailField,
                registerButton, messageLabel
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 350, 450);
        stage.setScene(scene);
        stage.show();
    }

    private boolean registerUser(String username, String password, String role, String phone, String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Kiểm tra trùng username
            String checkSql = "SELECT * FROM user WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return false;

            // Thêm user mới
            String insertSql = "INSERT INTO user(username, role, password, phone_number, email) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, role);
            insertStmt.setString(3, password);
            insertStmt.setString(4, phone);
            insertStmt.setString(5, email);
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
