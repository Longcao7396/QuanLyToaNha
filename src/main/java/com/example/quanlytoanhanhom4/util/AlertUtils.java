package com.example.quanlytoanhanhom4.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

/**
 * Utility class for displaying user-friendly alerts and dialogs.
 */
public final class AlertUtils {

    private AlertUtils() {
        // Utility class
    }

    /**
     * Shows an error alert dialog.
     * 
     * @param title The title of the alert
     * @param message The error message to display
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error alert with default title.
     * 
     * @param message The error message to display
     */
    public static void showError(String message) {
        showError("Lỗi", message);
    }

    /**
     * Shows a success/information alert dialog.
     * 
     * @param title The title of the alert
     * @param message The success message to display
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a success alert with default title.
     * 
     * @param message The success message to display
     */
    public static void showSuccess(String message) {
        showSuccess("Thành công", message);
    }

    /**
     * Shows a warning alert dialog.
     * 
     * @param title The title of the alert
     * @param message The warning message to display
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a warning alert with default title.
     * 
     * @param message The warning message to display
     */
    public static void showWarning(String message) {
        showWarning("Cảnh báo", message);
    }

    /**
     * Shows a confirmation dialog.
     * 
     * @param title The title of the dialog
     * @param message The confirmation message
     * @return true if user clicked OK, false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Shows a confirmation dialog with default title.
     * 
     * @param message The confirmation message
     * @return true if user clicked OK, false otherwise
     */
    public static boolean showConfirmation(String message) {
        return showConfirmation("Xác nhận", message);
    }

    /**
     * Shows an information alert dialog.
     * 
     * @param title The title of the alert
     * @param message The information message to display
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert with default title.
     * 
     * @param message The information message to display
     */
    public static void showInfo(String message) {
        showInfo("Thông tin", message);
    }
}

