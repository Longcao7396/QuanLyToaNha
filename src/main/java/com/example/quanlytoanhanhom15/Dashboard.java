package com.example.quanlytoanhanhom15;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Dashboard {

    public static void show(Stage owner, String role) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Dashboard - " + role);

            Label welcome = new Label("Chào mừng! Vai trò: " + role);
            Button logout = new Button("Đăng xuất");

            logout.setOnAction(e -> {
                stage.close();
                owner.show();
            });

            VBox layout = new VBox(10, welcome, logout);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20));

            Scene scene = new Scene(layout, 400, 300);
            stage.setScene(scene);
            stage.initOwner(owner);

            owner.hide();
            stage.show();

            stage.setOnCloseRequest(e -> owner.show());
        });
    }
}
