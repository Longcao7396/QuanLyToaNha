package com.example.quanlytoanhanhom4.util;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

/**
 * Utility class để load dữ liệu một cách tối ưu và đảm bảo hiển thị trên UI
 */
public class DataLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    
    /**
     * Load dữ liệu và cập nhật ObservableList một cách an toàn trên JavaFX Thread
     * 
     * @param <T> Type of data
     * @param dataSupplier Function để lấy dữ liệu từ service
     * @param observableList ObservableList để cập nhật
     * @param tableName Tên bảng để log
     */
    public static <T> void loadDataAsync(Supplier<List<T>> dataSupplier, 
                                         ObservableList<T> observableList, 
                                         String tableName) {
        logger.info("Bắt đầu load dữ liệu cho {}", tableName);
        
        Task<List<T>> loadTask = new Task<List<T>>() {
            @Override
            protected List<T> call() throws Exception {
                logger.debug("Đang load dữ liệu {} trong background thread...", tableName);
                List<T> data = dataSupplier.get();
                logger.info("Đã load được {} records cho {}", 
                    data != null ? data.size() : 0, tableName);
                return data;
            }
        };
        
        loadTask.setOnSucceeded(e -> {
            List<T> data = loadTask.getValue();
            if (data != null && !data.isEmpty()) {
                Platform.runLater(() -> {
                    observableList.clear();
                    observableList.addAll(data);
                    logger.info("Đã cập nhật {} records vào ObservableList cho {}", 
                        data.size(), tableName);
                });
            } else {
                logger.warn("Không có dữ liệu nào được trả về cho {}", tableName);
                Platform.runLater(() -> {
                    observableList.clear();
                });
            }
        });
        
        loadTask.setOnFailed(e -> {
            Throwable error = loadTask.getException();
            logger.error("Lỗi khi load dữ liệu cho {}: {}", tableName, error.getMessage(), error);
            Platform.runLater(() -> {
                observableList.clear();
            });
        });
        
        // Chạy task trong background thread
        Thread thread = new Thread(loadTask);
        thread.setDaemon(true);
        thread.start();
    }
    
    /**
     * Load dữ liệu đồng bộ (cho trường hợp đơn giản)
     * Đảm bảo update UI trên JavaFX Thread
     */
    public static <T> void loadDataSync(Supplier<List<T>> dataSupplier, 
                                        ObservableList<T> observableList, 
                                        String tableName) {
        logger.info("Bắt đầu load dữ liệu đồng bộ cho {}", tableName);
        
        try {
            List<T> data = dataSupplier.get();
            logger.info("Đã load được {} records cho {}", 
                data != null ? data.size() : 0, tableName);
            
            // Đảm bảo update trên JavaFX Thread
            Platform.runLater(() -> {
                observableList.clear();
                if (data != null && !data.isEmpty()) {
                    observableList.addAll(data);
                    logger.info("Đã cập nhật {} records vào ObservableList cho {}", 
                        data.size(), tableName);
                } else {
                    logger.warn("Không có dữ liệu nào được trả về cho {}", tableName);
                }
            });
        } catch (Exception e) {
            logger.error("Lỗi khi load dữ liệu cho {}: {}", tableName, e.getMessage(), e);
            Platform.runLater(() -> {
                observableList.clear();
            });
        }
    }
    
    /**
     * Load dữ liệu với delay để đảm bảo UI đã sẵn sàng
     */
    public static <T> void loadDataWithDelay(Supplier<List<T>> dataSupplier, 
                                              ObservableList<T> observableList, 
                                              String tableName, 
                                              long delayMs) {
        Platform.runLater(() -> {
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.millis(delayMs));
            pause.setOnFinished(e -> {
                loadDataSync(dataSupplier, observableList, tableName);
            });
            pause.play();
        });
    }
}
