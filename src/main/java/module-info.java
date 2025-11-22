module com.example.quanlytoanhanhom4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.quanlytoanhanhom4.controller to javafx.fxml;
    opens com.example.quanlytoanhanhom4.controller.auth to javafx.fxml;
    opens com.example.quanlytoanhanhom4.ui to javafx.fxml;
    opens com.example.quanlytoanhanhom4.ui.auth to javafx.fxml;

    exports com.example.quanlytoanhanhom4.app;
    exports com.example.quanlytoanhanhom4.config;
    exports com.example.quanlytoanhanhom4.controller;
    exports com.example.quanlytoanhanhom4.controller.auth;
    exports com.example.quanlytoanhanhom4.model;
    exports com.example.quanlytoanhanhom4.service;
    exports com.example.quanlytoanhanhom4.service.auth;
    exports com.example.quanlytoanhanhom4.ui;
    exports com.example.quanlytoanhanhom4.ui.auth;
    exports com.example.quanlytoanhanhom4.util;
}