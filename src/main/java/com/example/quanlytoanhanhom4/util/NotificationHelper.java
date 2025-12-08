package com.example.quanlytoanhanhom4.util;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Utility class for displaying non-blocking snackbar-style notifications.
 * These notifications appear in the top-right corner and auto-dismiss after a few seconds.
 */
public final class NotificationHelper {

    private NotificationHelper() {
        // Utility class
    }

    /**
     * Shows a success notification in the top-right corner.
     * 
     * @param message The success message to display
     */
    public static void showSuccess(String message) {
        showSuccess("Thành công", message);
    }

    /**
     * Shows a success notification with custom title.
     * 
     * @param title The title of the notification
     * @param message The success message to display
     */
    public static void showSuccess(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .darkStyle()
                .showInformation();
    }

    /**
     * Shows an error notification in the top-right corner.
     * 
     * @param message The error message to display
     */
    public static void showError(String message) {
        showError("Lỗi", message);
    }

    /**
     * Shows an error notification with custom title.
     * 
     * @param title The title of the notification
     * @param message The error message to display
     */
    public static void showError(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(4))
                .darkStyle()
                .showError();
    }

    /**
     * Shows a warning notification in the top-right corner.
     * 
     * @param message The warning message to display
     */
    public static void showWarning(String message) {
        showWarning("Cảnh báo", message);
    }

    /**
     * Shows a warning notification with custom title.
     * 
     * @param title The title of the notification
     * @param message The warning message to display
     */
    public static void showWarning(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .darkStyle()
                .showWarning();
    }

    /**
     * Shows an information notification in the top-right corner.
     * 
     * @param message The information message to display
     */
    public static void showInfo(String message) {
        showInfo("Thông tin", message);
    }

    /**
     * Shows an information notification with custom title.
     * 
     * @param title The title of the notification
     * @param message The information message to display
     */
    public static void showInfo(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(3))
                .darkStyle()
                .showInformation();
    }
}






