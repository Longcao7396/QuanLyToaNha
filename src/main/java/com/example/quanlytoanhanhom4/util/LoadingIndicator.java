package com.example.quanlytoanhanhom4.util;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Utility class for showing loading indicators during long operations.
 */
public final class LoadingIndicator {

    private static Stage loadingStage;
    private static ProgressIndicator progressIndicator;

    private LoadingIndicator() {
        // Utility class
    }

    /**
     * Shows a loading indicator in a modal dialog.
     * 
     * @param parentStage The parent stage (can be null)
     * @param message Optional message to display (can be null)
     */
    public static void show(Stage parentStage, String message) {
        Platform.runLater(() -> {
            if (loadingStage != null && loadingStage.isShowing()) {
                return; // Already showing
            }

            progressIndicator = new ProgressIndicator();
            progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            progressIndicator.setStyle("-fx-progress-color: #3498db;");

            StackPane root = new StackPane(progressIndicator);
            root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
            root.setPrefSize(200, 200);

            Scene scene = new Scene(root, 200, 200);
            scene.setFill(Color.TRANSPARENT);

            loadingStage = new Stage();
            loadingStage.initStyle(StageStyle.TRANSPARENT);
            loadingStage.initModality(Modality.APPLICATION_MODAL);
            if (parentStage != null) {
                loadingStage.initOwner(parentStage);
            }
            loadingStage.setScene(scene);
            loadingStage.setAlwaysOnTop(true);
            
            // Center the loading indicator
            if (parentStage != null) {
                loadingStage.setX(parentStage.getX() + parentStage.getWidth() / 2 - 100);
                loadingStage.setY(parentStage.getY() + parentStage.getHeight() / 2 - 100);
            }
            
            loadingStage.show();
        });
    }

    /**
     * Shows a loading indicator with default message.
     * 
     * @param parentStage The parent stage
     */
    public static void show(Stage parentStage) {
        show(parentStage, null);
    }

    /**
     * Hides the loading indicator.
     */
    public static void hide() {
        Platform.runLater(() -> {
            if (loadingStage != null) {
                loadingStage.close();
                loadingStage = null;
                progressIndicator = null;
            }
        });
    }

    /**
     * Executes a task with a loading indicator.
     * 
     * @param parentStage The parent stage
     * @param task The task to execute
     * @param <T> The return type
     * @return The result of the task
     * @throws Exception if the task fails
     */
    public static <T> T executeWithLoading(Stage parentStage, LoadingTask<T> task) throws Exception {
        show(parentStage);
        try {
            return task.execute();
        } finally {
            hide();
        }
    }

    /**
     * Functional interface for tasks that should show loading indicator.
     */
    @FunctionalInterface
    public interface LoadingTask<T> {
        T execute() throws Exception;
    }
}

