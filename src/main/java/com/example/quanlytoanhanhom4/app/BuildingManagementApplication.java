package com.example.quanlytoanhanhom4.app;

import com.example.quanlytoanhanhom4.config.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BuildingManagementApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quanlytoanhanhom4/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 350);
        stage.setTitle("Đăng nhập quản lý toà nhà");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        // Tự động import dữ liệu mẫu nếu database trống
        DatabaseInitializer.checkAndImportSampleData();
        launch(args);
    }
}


