package com.example.quanlytoanhanhom4.tool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * UI Tool để import SQL migration files vào database
 */
public class ImportMigrationTool extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(ImportMigrationTool.class);
    
    private TextArea logArea;
    private Button importFromResourcesBtn;
    private Button importFromDirectoryBtn;
    private ProgressBar progressBar;
    private Label statusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tool Import SQL Migration");
        
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        // Tiêu đề
        Label titleLabel = new Label("🚀 TOOL TỰ ĐỘNG IMPORT SQL MIGRATION");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2874A6;");
        
        // Panel điều khiển
        HBox controlPanel = new HBox(15);
        controlPanel.setAlignment(Pos.CENTER);
        
        importFromResourcesBtn = new Button("📁 Import từ Resources");
        importFromResourcesBtn.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " +
                                       "-fx-font-size: 14px; -fx-font-weight: bold; " +
                                       "-fx-padding: 10 20; -fx-background-radius: 5;");
        importFromResourcesBtn.setOnAction(e -> importFromResources());
        
        importFromDirectoryBtn = new Button("📂 Import từ Thư mục");
        importFromDirectoryBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " +
                                       "-fx-font-size: 14px; -fx-font-weight: bold; " +
                                       "-fx-padding: 10 20; -fx-background-radius: 5;");
        importFromDirectoryBtn.setOnAction(e -> importFromDirectory());
        
        controlPanel.getChildren().addAll(importFromResourcesBtn, importFromDirectoryBtn);
        
        // Progress bar
        progressBar = new ProgressBar();
        progressBar.setVisible(false);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        
        // Status label
        statusLabel = new Label("Sẵn sàng import...");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        
        // Log area
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");
        VBox.setVgrow(logArea, Priority.ALWAYS);
        
        // Scroll to bottom button
        Button clearLogBtn = new Button("🗑️ Xóa Log");
        clearLogBtn.setOnAction(e -> logArea.clear());
        
        HBox logHeader = new HBox(10);
        logHeader.setAlignment(Pos.CENTER_LEFT);
        logHeader.getChildren().addAll(new Label("📋 Log:"), new Region(), clearLogBtn);
        HBox.setHgrow(logHeader.getChildren().get(1), Priority.ALWAYS);
        
        root.getChildren().addAll(
            titleLabel,
            controlPanel,
            progressBar,
            statusLabel,
            logHeader,
            logArea
        );
        
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        appendLog("✅ Tool đã sẵn sàng!");
        appendLog("Chọn một trong các tùy chọn:");
        appendLog("  - Import từ Resources: Import từ thư mục resources/db/migration");
        appendLog("  - Import từ Thư mục: Chọn thư mục chứa file SQL để import\n");
    }
    
    private void importFromResources() {
        setButtonsDisabled(true);
        statusLabel.setText("Đang import từ resources...");
        progressBar.setVisible(true);
        progressBar.setProgress(-1); // Indeterminate
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                return SqlMigrationImporter.importAllFromResources(true);
            }
        };
        
        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            setButtonsDisabled(false);
            progressBar.setVisible(false);
            
            if (success) {
                statusLabel.setText("✅ Import thành công!");
                appendLog("\n✅ HOÀN THÀNH! Tất cả file đã được import thành công.\n");
            } else {
                statusLabel.setText("❌ Import có lỗi!");
                appendLog("\n❌ CÓ LỖI XẢY RA! Vui lòng kiểm tra log phía trên.\n");
            }
        });
        
        task.setOnFailed(e -> {
            setButtonsDisabled(false);
            progressBar.setVisible(false);
            statusLabel.setText("❌ Lỗi khi import!");
            appendLog("\n❌ LỖI: " + task.getException().getMessage() + "\n");
        });
        
        new Thread(task).start();
    }
    
    private void importFromDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Chọn thư mục chứa file SQL");
        
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {
            return;
        }
        
        setButtonsDisabled(true);
        statusLabel.setText("Đang import từ thư mục: " + selectedDirectory.getAbsolutePath());
        progressBar.setVisible(true);
        progressBar.setProgress(-1); // Indeterminate
        
        appendLog("\n📂 Đã chọn thư mục: " + selectedDirectory.getAbsolutePath() + "\n");
        
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                return SqlMigrationImporter.importAllFromDirectory(selectedDirectory.getAbsolutePath(), true);
            }
        };
        
        task.setOnSucceeded(e -> {
            boolean success = task.getValue();
            setButtonsDisabled(false);
            progressBar.setVisible(false);
            
            if (success) {
                statusLabel.setText("✅ Import thành công!");
                appendLog("\n✅ HOÀN THÀNH! Tất cả file đã được import thành công.\n");
            } else {
                statusLabel.setText("❌ Import có lỗi!");
                appendLog("\n❌ CÓ LỖI XẢY RA! Vui lòng kiểm tra log phía trên.\n");
            }
        });
        
        task.setOnFailed(e -> {
            setButtonsDisabled(false);
            progressBar.setVisible(false);
            statusLabel.setText("❌ Lỗi khi import!");
            appendLog("\n❌ LỖI: " + task.getException().getMessage() + "\n");
        });
        
        new Thread(task).start();
    }
    
    private void setButtonsDisabled(boolean disabled) {
        importFromResourcesBtn.setDisable(disabled);
        importFromDirectoryBtn.setDisable(disabled);
    }
    
    private void appendLog(String text) {
        Platform.runLater(() -> {
            logArea.appendText(text + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}


