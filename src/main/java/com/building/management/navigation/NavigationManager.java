package com.building.management.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Navigation Manager for FXML screen switching
 * Handles all navigation between screens
 */
public class NavigationManager {
    private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
    private static Stage primaryStage;

    /**
     * Set primary stage (called once from Main)
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Switch to a new FXML screen
     * @param fxmlPath Path to FXML file (e.g., "/fxml/dashboard.fxml")
     * @param title Window title
     */
    public static void switchTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            
            // Apply CSS
            scene.getStylesheets().add(NavigationManager.class.getResource("/css/style.css").toExternalForm());
            
            if (primaryStage != null) {
                primaryStage.setScene(scene);
                primaryStage.setTitle(title);
                primaryStage.setMaximized(true);
                primaryStage.show();
            } else {
                logger.error("Primary stage is null. Cannot switch to: {}", fxmlPath);
            }
        } catch (IOException e) {
            logger.error("Failed to load FXML: {}", fxmlPath, e);
            throw new RuntimeException("Navigation failed: " + fxmlPath, e);
        }
    }

    /**
     * Switch to login screen
     */
    public static void switchToLogin() {
        switchTo("/fxml/login.fxml", "Building Management System - Login");
    }

    /**
     * Switch to dashboard
     */
    public static void switchToDashboard() {
        switchTo("/fxml/dashboard.fxml", "Building Management System - Dashboard");
    }
}


