package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.model.ApartmentServiceFee;
import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.model.Resident;
import com.example.quanlytoanhanhom4.model.ServiceFeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service tự động kiểm tra và gửi email thông báo về phí dịch vụ điện nước
 * khi đến ngày phải đóng
 */
public class ServiceFeeReminderService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFeeReminderService.class);
    
    private static ScheduledExecutorService scheduler;
    private static boolean isRunning = false;
    
    /**
     * Khởi động service tự động kiểm tra và gửi email
     * Kiểm tra mỗi ngày lúc 8:00 sáng
     */
    public static void startDailyReminder() {
        if (isRunning) {
            logger.warn("ServiceFeeReminderService đã đang chạy. Bỏ qua khởi động lại.");
            return;
        }
        
        if (!EmailService.isEmailServiceReady()) {
            logger.warn("Email service chưa sẵn sàng. ServiceFeeReminderService sẽ không được khởi động.");
            return;
        }
        
        scheduler = Executors.newScheduledThreadPool(1);
        
        // Tính toán thời gian đến 8:00 sáng hôm sau
        long initialDelay = calculateInitialDelay();
        long period = 24 * 60 * 60; // 24 giờ (tính bằng giây)
        
        scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    checkAndSendReminders();
                } catch (Exception e) {
                    logger.error("Lỗi khi chạy scheduled task kiểm tra phí dịch vụ: {}", e.getMessage(), e);
                }
            },
            initialDelay,
            period,
            TimeUnit.SECONDS
        );
        
        isRunning = true;
        logger.info("ServiceFeeReminderService đã được khởi động. Sẽ kiểm tra và gửi email mỗi ngày lúc 8:00 sáng.");
    }
    
    /**
     * Tính toán thời gian delay đến 8:00 sáng hôm sau (tính bằng giây)
     */
    private static long calculateInitialDelay() {
        java.time.LocalTime now = java.time.LocalTime.now();
        java.time.LocalTime targetTime = java.time.LocalTime.of(8, 0); // 8:00 sáng
        
        long delay;
        if (now.isBefore(targetTime)) {
            // Nếu hiện tại trước 8:00 sáng, chạy lúc 8:00 sáng hôm nay
            delay = java.time.Duration.between(now, targetTime).getSeconds();
        } else {
            // Nếu đã qua 8:00 sáng, chạy lúc 8:00 sáng ngày mai
            delay = java.time.Duration.between(now, targetTime).plusHours(24).getSeconds();
        }
        
        return delay;
    }
    
    /**
     * Kiểm tra và gửi email thông báo cho các phí dịch vụ sắp đến hạn hoặc đã đến hạn
     */
    public static void checkAndSendReminders() {
        logger.info("Bắt đầu kiểm tra phí dịch vụ sắp đến hạn...");
        
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate threeDaysLater = today.plusDays(3);
        
        // Lấy tất cả phí dịch vụ chưa thanh toán
        List<ApartmentServiceFee> allServiceFees = ApartmentServiceFeeService.getAllServiceFees();
        
        int sentCount = 0;
        int errorCount = 0;
        
        for (ApartmentServiceFee fee : allServiceFees) {
            // Chỉ xử lý các phí chưa thanh toán
            if (fee.getStatus() == null || 
                fee.getStatus().equals("PAID") || 
                fee.getStatus().equals("ĐÃ_THANH_TOÁN") ||
                fee.getStatus().equals("CANCELLED")) {
                continue;
            }
            
            if (fee.getDueDate() == null) {
                continue;
            }
            
            LocalDate dueDate = fee.getDueDate();
            
            // Gửi email nếu:
            // 1. Đã đến hạn (hôm nay)
            // 2. Sắp đến hạn (trong 3 ngày tới)
            
            boolean shouldSend = false;
            boolean isDue = false;
            
            if (dueDate.isEqual(today)) {
                // Đã đến hạn
                shouldSend = true;
                isDue = true;
            } else if (dueDate.isAfter(today) && dueDate.isBefore(threeDaysLater) || 
                      dueDate.isEqual(tomorrow)) {
                // Sắp đến hạn (trong 3 ngày)
                shouldSend = true;
                isDue = false;
            } else if (dueDate.isBefore(today)) {
                // Đã quá hạn - gửi email thông báo quá hạn
                shouldSend = true;
                isDue = true;
            }
            
            if (shouldSend) {
                if (sendReminderEmail(fee, isDue)) {
                    sentCount++;
                } else {
                    errorCount++;
                }
            }
        }
        
        logger.info("Hoàn thành kiểm tra phí dịch vụ. Đã gửi {} email thành công, {} email lỗi.", sentCount, errorCount);
    }
    
    /**
     * Gửi email thông báo cho một phí dịch vụ cụ thể
     */
    private static boolean sendReminderEmail(ApartmentServiceFee fee, boolean isDue) {
        try {
            // Lấy thông tin căn hộ
            Apartment apartment = ApartmentService.getApartmentById(fee.getApartmentId());
            if (apartment == null) {
                logger.warn("Không tìm thấy căn hộ với ID: {}", fee.getApartmentId());
                return false;
            }
            
            // Lấy thông tin cư dân (chủ hộ)
            Resident resident = ResidentService.getResidentByApartmentId(fee.getApartmentId());
            if (resident == null || resident.getEmail() == null || resident.getEmail().trim().isEmpty()) {
                logger.warn("Không tìm thấy email của cư dân cho căn hộ ID: {}", fee.getApartmentId());
                return false;
            }
            
            // Lấy thông tin loại phí
            ServiceFeeType feeType = ServiceFeeTypeService.getFeeTypeById(fee.getFeeTypeId());
            String feeTypeName = feeType != null ? feeType.getFeeName() : "Phí dịch vụ";
            
            // Format ngày
            String dueDateStr = fee.getDueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            // Gửi email
            boolean success;
            if (isDue) {
                success = EmailService.sendServiceFeeDueEmail(
                    resident.getEmail(),
                    resident.getFullName(),
                    apartment.getApartmentCode() != null ? apartment.getApartmentCode() : 
                        (apartment.getApartmentNo() != null ? apartment.getApartmentNo() : "N/A"),
                    feeTypeName,
                    fee.getTotalAmount() != null ? fee.getTotalAmount() : 0.0,
                    dueDateStr
                );
            } else {
                success = EmailService.sendServiceFeeReminderEmail(
                    resident.getEmail(),
                    resident.getFullName(),
                    apartment.getApartmentCode() != null ? apartment.getApartmentCode() : 
                        (apartment.getApartmentNo() != null ? apartment.getApartmentNo() : "N/A"),
                    feeTypeName,
                    fee.getTotalAmount() != null ? fee.getTotalAmount() : 0.0,
                    dueDateStr
                );
            }
            
            if (success) {
                logger.info("Đã gửi email thông báo phí dịch vụ đến: {} ({})", 
                    resident.getEmail(), resident.getFullName());
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email thông báo cho phí dịch vụ ID {}: {}", 
                fee.getId(), e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Kiểm tra và gửi email ngay lập tức (không cần đợi scheduled task)
     * Hữu ích để test hoặc chạy thủ công
     */
    public static void checkAndSendRemindersNow() {
        new Thread(() -> {
            logger.info("Chạy kiểm tra và gửi email thông báo ngay lập tức...");
            checkAndSendReminders();
        }).start();
    }
    
    /**
     * Dừng service tự động
     */
    public static void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            isRunning = false;
            logger.info("ServiceFeeReminderService đã được dừng.");
        }
    }
    
    /**
     * Kiểm tra xem service có đang chạy không
     */
    public static boolean isRunning() {
        return isRunning;
    }
}

