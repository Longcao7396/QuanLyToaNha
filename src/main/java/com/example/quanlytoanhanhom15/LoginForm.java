package com.example.quanlytoanhanhom15;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class LoginForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Đăng nhập");

        Label userLabel = new Label("Tên đăng nhập:");
        TextField userField = new TextField();
        Label passLabel = new Label("Mật khẩu:");
        PasswordField passField = new PasswordField();

        Button loginButton = new Button("Đăng nhập");
        Button registerButton = new Button("Đăng ký");

        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            String role = verifyLogin(username, password);
            if (role != null) {
                messageLabel.setText("✅ Đăng nhập thành công! (Vai trò: " + role + ")");
                Dashboard.show(primaryStage, role); // mở cửa sổ mới và ẩn login
            } else {
                messageLabel.setText("❌ Sai tên đăng nhập hoặc mật khẩu!");
            }
        });

        registerButton.setOnAction(e -> {
            RegisterForm registerForm = new RegisterForm();
            try {
                registerForm.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginButton, registerButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 320, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String verifyLogin(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT role FROM user WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); // Trả về vai trò nếu đăng nhập đúng
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
