package com.example.quanlytoanhanhom4.service;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service để hiển thị đồng hồ địa phương với cập nhật real-time
 */
public final class ClockService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClockService.class);
    private static final DateTimeFormatter TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("HH:mm:ss", Locale.forLanguageTag("vi"));
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm:ss", Locale.forLanguageTag("vi"));
    
    private ClockService() {
        // Utility class
    }
    
    /**
     * Tạo Label với đồng hồ cập nhật mỗi giây
     * 
     * @param showDate true nếu muốn hiển thị cả ngày tháng
     * @return Label với đồng hồ đang chạy
     */
    public static Label createClockLabel(boolean showDate) {
        Label clockLabel = new Label();
        clockLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 500;");
        
        // Cập nhật ngay lập tức
        updateClockLabel(clockLabel, showDate);
        
        // Tạo Timeline để cập nhật mỗi giây
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> updateClockLabel(clockLabel, showDate))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        logger.debug("Đã tạo đồng hồ với cập nhật real-time");
        
        return clockLabel;
    }
    
    /**
     * Cập nhật text của Label với thời gian hiện tại
     */
    private static void updateClockLabel(Label label, boolean showDate) {
        try {
            LocalDateTime now = LocalDateTime.now();
            if (showDate) {
                label.setText(now.format(DATE_TIME_FORMATTER));
            } else {
                label.setText(now.format(TIME_FORMATTER));
            }
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật đồng hồ", e);
            label.setText("--:--:--");
        }
    }
    
    /**
     * Lấy thời gian hiện tại dạng string
     */
    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }
    
    /**
     * Lấy ngày giờ hiện tại dạng string
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }
}





