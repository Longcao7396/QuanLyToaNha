package com.building.management;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main entry point for Building Management System
 * Java 17 + JavaFX Application
 */
public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Starting Building Management System...");
            
            // Initialize database
            com.building.management.config.DatabaseConfig.initialize();
            
            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 500);
            
            // Apply CSS
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            primaryStage.setTitle("Building Management System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // Set primary stage for NavigationManager
            com.building.management.navigation.NavigationManager.setPrimaryStage(primaryStage);
            
            primaryStage.show();
            
            logger.info("Application started successfully");
        } catch (IOException e) {
            logger.error("Failed to load login screen", e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

