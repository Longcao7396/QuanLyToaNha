package com.example.quanlytoanhanhom4.ui;

import com.example.quanlytoanhanhom4.service.WeatherService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component thời tiết đồng nhất và gọn gàng
 * Hiển thị: Icon + Nhiệt độ + Tên thành phố + Mô tả
 */
public class WeatherWidget {
    
    private static final Logger logger = LoggerFactory.getLogger(WeatherWidget.class);
    
    /**
     * Tạo component thời tiết đồng nhất
     * @param compact true = hiển thị gọn (cho top bar), false = hiển thị đầy đủ (cho dashboard)
     * @return HBox chứa thông tin thời tiết
     */
    public static HBox create(boolean compact) {
        HBox weatherBox = new HBox(compact ? 8 : 10);
        weatherBox.setAlignment(Pos.CENTER);
        weatherBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 8; -fx-padding: 6 12;");
        
        // Placeholder khi đang tải
        Label loadingText = new Label("Đang tải...");
        loadingText.setStyle("-fx-text-fill: white; -fx-font-size: " + (compact ? "12px" : "14px") + ";");
        weatherBox.getChildren().add(loadingText);
        
        // Lấy thông tin thời tiết trong background
        Task<WeatherService.WeatherInfo> weatherTask = new Task<WeatherService.WeatherInfo>() {
            @Override
            protected WeatherService.WeatherInfo call() throws Exception {
                return WeatherService.getCurrentWeather();
            }
        };
        
        weatherTask.setOnSucceeded(e -> {
            try {
                WeatherService.WeatherInfo weather = weatherTask.getValue();
                if (weather != null) {
                    HBox finalWidget = createWeatherWidget(weather, compact);
                    Platform.runLater(() -> {
                        weatherBox.getChildren().clear();
                        weatherBox.getChildren().addAll(finalWidget.getChildren());
                    });
                } else {
                    showError(weatherBox, compact);
                }
            } catch (Exception ex) {
                logger.error("Lỗi khi tạo widget thời tiết", ex);
                showError(weatherBox, compact);
            }
        });
        
        weatherTask.setOnFailed(e -> {
            logger.error("Lỗi khi lấy thời tiết", weatherTask.getException());
            showError(weatherBox, compact);
        });
        
        new Thread(weatherTask).start();
        
        return weatherBox;
    }
    
    /**
     * Tạo widget thời tiết với dữ liệu
     */
    private static HBox createWeatherWidget(WeatherService.WeatherInfo weather, boolean compact) {
        HBox widget = new HBox(compact ? 8 : 10);
        widget.setAlignment(Pos.CENTER);
        
        // Icon thời tiết từ WeatherAPI.com
        int iconSize = compact ? 36 : 44;
        ImageView iconView = new ImageView();
        iconView.setFitWidth(iconSize);
        iconView.setFitHeight(iconSize);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.setCache(true);
        
        try {
            String iconUrl = weather.getIconUrl();
            Image iconImage = new Image(iconUrl, true); // true = background loading
            iconView.setImage(iconImage);
        } catch (Exception e) {
            logger.debug("Không thể load icon thời tiết: {}", e.getMessage());
        }
        
        widget.getChildren().add(iconView);
        
        // Nhiệt độ
        Label tempLabel = new Label(weather.getFormattedTemperature());
        tempLabel.setStyle("-fx-text-fill: white; -fx-font-size: " + (compact ? "16px" : "20px") + "; -fx-font-weight: bold;");
        
        // Chi tiết
        VBox detailsBox = new VBox(compact ? 2 : 3);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        
        String cityName = weather.getCity();
        String desc = weather.getFormattedDescription();
        if (desc == null || desc.isEmpty()) {
            desc = "Nắng";
        }
        desc = WeatherService.translateToVietnamese(desc);
        if (!desc.isEmpty()) {
            desc = desc.substring(0, 1).toUpperCase() + desc.substring(1);
        }
        
        // Kiểm tra xem có tên thành phố hợp lệ không
        boolean hasValidCity = cityName != null && 
                               !cityName.isEmpty() && 
                               !cityName.equals("Vị trí hiện tại") && 
                               !cityName.equals("Đang xác định...") &&
                               cityName.length() > 2;
        
        if (hasValidCity) {
            // Hiển thị đầy đủ: Tên thành phố + Mô tả
            Label cityLabel = new Label(cityName);
            cityLabel.setStyle("-fx-text-fill: white; -fx-font-size: " + (compact ? "12px" : "14px") + "; -fx-font-weight: bold;");
            
            Label descLabel = new Label(desc);
            descLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: " + (compact ? "11px" : "13px") + ";");
            
            detailsBox.getChildren().addAll(cityLabel, descLabel);
        } else {
            // Nếu không có tên thành phố hợp lệ, chỉ hiển thị mô tả thời tiết với font lớn hơn
            Label descLabel = new Label(desc);
            descLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: " + (compact ? "12px" : "14px") + "; -fx-font-weight: normal;");
            detailsBox.getChildren().add(descLabel);
        }
        
        widget.getChildren().addAll(tempLabel, detailsBox);
        
        return widget;
    }
    
    /**
     * Hiển thị lỗi
     */
    private static void showError(HBox weatherBox, boolean compact) {
        Platform.runLater(() -> {
            weatherBox.getChildren().clear();
            Label errorText = new Label("Không tải được");
            errorText.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: " + (compact ? "12px" : "14px") + ";");
            weatherBox.getChildren().add(errorText);
        });
    }
}

