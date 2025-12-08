package com.example.quanlytoanhanhom4.ui.auth;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class RegisterForm extends Application {

    private static final Logger logger = LoggerFactory.getLogger(RegisterForm.class);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quanlytoanhanhom4/fxml/register.fxml"));
            Scene scene = new Scene(loader.load(), 520, 750);
            
            // Load CSS files
            try {
                String modernCss = Objects.requireNonNull(getClass().getResource("/css/modern-style.css")).toExternalForm();
                String registerCss = Objects.requireNonNull(getClass().getResource("/css/register-style.css")).toExternalForm();
                scene.getStylesheets().addAll(modernCss, registerCss);
                logger.debug("Đã load CSS files thành công");
            } catch (NullPointerException e) {
                logger.warn("Không tìm thấy một số CSS files, sẽ tiếp tục không CSS", e);
            }
            
            // Set application icon
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/quanlytoanhanhom4/images/building_icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                logger.debug("Không tìm thấy icon ứng dụng", e);
            }
            
            stage.setTitle("Đăng ký tài khoản");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            
            logger.debug("Mở màn hình đăng ký thành công");
        } catch (Exception e) {
            logger.error("Lỗi khi mở màn hình đăng ký", e);
            e.printStackTrace();
            throw new RuntimeException("Không thể mở màn hình đăng ký: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
