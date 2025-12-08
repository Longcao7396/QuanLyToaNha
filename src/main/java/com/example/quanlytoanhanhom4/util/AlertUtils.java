package com.example.quanlytoanhanhom4.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

/**
 * Utility class for displaying user-friendly alerts and dialogs.
 * Success and error messages use non-blocking notifications (snackbar-style).
 * Confirmation dialogs remain blocking for user interaction.
 */
public final class AlertUtils {

    private AlertUtils() {
        // Utility class
    }

    /**
     * Shows an error notification (non-blocking snackbar in top-right corner).
     * 
     * @param title The title of the notification
     * @param message The error message to display
     */
    public static void showError(String title, String message) {
        NotificationHelper.showError(title, message);
    }

    /**
     * Shows an error notification with default title (non-blocking).
     * 
     * @param message The error message to display
     */
    public static void showError(String message) {
        NotificationHelper.showError(message);
    }

    /**
     * Shows a success notification (non-blocking snackbar in top-right corner).
     * 
     * @param title The title of the notification
     * @param message The success message to display
     */
    public static void showSuccess(String title, String message) {
        NotificationHelper.showSuccess(title, message);
    }

    /**
     * Shows a success notification with default title (non-blocking).
     * 
     * @param message The success message to display
     */
    public static void showSuccess(String message) {
        NotificationHelper.showSuccess(message);
    }

    /**
     * Shows a warning notification (non-blocking snackbar in top-right corner).
     * For critical warnings that require user attention, consider using showWarningDialog instead.
     * 
     * @param title The title of the notification
     * @param message The warning message to display
     */
    public static void showWarning(String title, String message) {
        NotificationHelper.showWarning(title, message);
    }

    /**
     * Shows a warning notification with default title (non-blocking).
     * 
     * @param message The warning message to display
     */
    public static void showWarning(String message) {
        NotificationHelper.showWarning(message);
    }

    /**
     * Shows a blocking warning dialog (for critical warnings that require user acknowledgment).
     * 
     * @param title The title of the alert
     * @param message The warning message to display
     */
    public static void showWarningDialog(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
     * Shows an information notification (non-blocking snackbar in top-right corner).
     * 
     * @param title The title of the notification
     * @param message The information message to display
     */
    public static void showInfo(String title, String message) {
        NotificationHelper.showInfo(title, message);
    }

    /**
     * Shows an information notification with default title (non-blocking).
     * 
     * @param message The information message to display
     */
    public static void showInfo(String message) {
        NotificationHelper.showInfo(message);
    }
}

