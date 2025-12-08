package com.building.management.controller;

import com.building.management.navigation.NavigationManager;
import com.building.management.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Dashboard Controller
 * Main screen after login
 * Handles navigation to different modules
 */
public class DashboardController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var currentUser = UserService.getCurrentUser();
        if (currentUser != null) {
            welcomeLabel.setText("Chào mừng, " + currentUser.getFullName() + "!");
        }
        logger.debug("DashboardController initialized");
    }

    @FXML
    private void handleResidents() {
        logger.info("Navigate to Residents module");
        // TODO: Load residents module
        // NavigationManager.switchTo("/fxml/residents.fxml", "Residents Management");
    }

    @FXML
    private void handleApartments() {
        logger.info("Navigate to Apartments module");
        // TODO: Load apartments module
    }

    @FXML
    private void handleFees() {
        logger.info("Navigate to Fees module");
        // TODO: Load fees module
    }

    @FXML
    private void handleMaintenance() {
        logger.info("Navigate to Maintenance module");
        // TODO: Load maintenance module
    }

    @FXML
    private void handleStaff() {
        logger.info("Navigate to Staff module");
        // TODO: Load staff module
    }

    @FXML
    private void handleLogout() {
        logger.info("User logging out");
        UserService.logout();
        NavigationManager.switchToLogin();
    }
}


