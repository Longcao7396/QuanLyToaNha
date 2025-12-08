package com.example.quanlytoanhanhom4.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

/**
 * Service để gửi email thông báo đến cư dân
 * Module: Quản lý phí dịch vụ điện nước
 */
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private static boolean emailEnabled = true;
    private static String smtpHost = "smtp.gmail.com";
    private static int smtpPort = 587;
    private static boolean smtpAuth = true;
    private static boolean smtpStartTLS = true;
    private static String emailUsername = "";
    private static String emailPassword = "";
    private static String emailFromName = "Quản Lý Tòa Nhà";
    private static String emailFromAddress = "";
    
    private static boolean initialized = false;
    
    static {
        loadEmailConfig();
    }
    
    /**
     * Load cấu hình email từ application.properties
     */
    private static void loadEmailConfig() {
        try (InputStream input = EmailService.class
                .getResourceAsStream("/application.properties")) {
            
            Properties props = new Properties();
            props.load(input);
            
            emailEnabled = Boolean.parseBoolean(props.getProperty("email.enabled", "true"));
            smtpHost = props.getProperty("email.smtp.host", "smtp.gmail.com");
            smtpPort = Integer.parseInt(props.getProperty("email.smtp.port", "587"));
            smtpAuth = Boolean.parseBoolean(props.getProperty("email.smtp.auth", "true"));
            smtpStartTLS = Boolean.parseBoolean(props.getProperty("email.smtp.starttls.enable", "true"));
            emailUsername = props.getProperty("email.username", "");
            emailPassword = props.getProperty("email.password", "");
            emailFromName = props.getProperty("email.from.name", "Quản Lý Tòa Nhà");
            emailFromAddress = props.getProperty("email.from.address", "");
            
            initialized = true;
            logger.info("Đã load cấu hình email. Email enabled: {}", emailEnabled);
            
        } catch (Exception e) {
            logger.error("Lỗi khi load cấu hình email từ application.properties", e);
            emailEnabled = false;
        }
    }
    
    /**
     * Gửi email thông báo về phí dịch vụ điện nước sắp đến hạn
     */
    public static boolean sendServiceFeeReminderEmail(String toEmail, String residentName, 
                                                     String apartmentCode, String feeTypeName,
                                                     double totalAmount, String dueDate) {
        if (!emailEnabled) {
            logger.warn("Email service không được bật. Bỏ qua gửi email đến: {}", toEmail);
            return false;
        }
        
        if (toEmail == null || toEmail.trim().isEmpty()) {
            logger.warn("Email người nhận trống. Bỏ qua gửi email.");
            return false;
        }
        
        if (emailUsername == null || emailUsername.trim().isEmpty() || 
            emailPassword == null || emailPassword.trim().isEmpty()) {
            logger.warn("Email username hoặc password chưa được cấu hình. Vui lòng cập nhật application.properties");
            return false;
        }
        
        String subject = "Thông báo: Phí dịch vụ " + feeTypeName + " sắp đến hạn thanh toán";
        String body = buildServiceFeeReminderEmailBody(residentName, apartmentCode, 
                                                       feeTypeName, totalAmount, dueDate);
        
        return sendEmail(toEmail, subject, body);
    }
    
    /**
     * Gửi email thông báo về phí dịch vụ điện nước đã đến hạn
     */
    public static boolean sendServiceFeeDueEmail(String toEmail, String residentName,
                                                 String apartmentCode, String feeTypeName,
                                                 double totalAmount, String dueDate) {
        if (!emailEnabled) {
            logger.warn("Email service không được bật. Bỏ qua gửi email đến: {}", toEmail);
            return false;
        }
        
        if (toEmail == null || toEmail.trim().isEmpty()) {
            logger.warn("Email người nhận trống. Bỏ qua gửi email.");
            return false;
        }
        
        if (emailUsername == null || emailUsername.trim().isEmpty() || 
            emailPassword == null || emailPassword.trim().isEmpty()) {
            logger.warn("Email username hoặc password chưa được cấu hình. Vui lòng cập nhật application.properties");
            return false;
        }
        
        String subject = "THÔNG BÁO KHẨN: Phí dịch vụ " + feeTypeName + " đã đến hạn thanh toán";
        String body = buildServiceFeeDueEmailBody(residentName, apartmentCode,
                                                  feeTypeName, totalAmount, dueDate);
        
        return sendEmail(toEmail, subject, body);
    }
    
    /**
     * Xây dựng nội dung email nhắc nhở phí dịch vụ sắp đến hạn
     */
    private static String buildServiceFeeReminderEmailBody(String residentName, String apartmentCode,
                                                          String feeTypeName, double totalAmount,
                                                          String dueDate) {
        return String.format(
            "Kính gửi ông/bà %s,\n\n" +
            "Ban quản lý tòa nhà xin thông báo:\n\n" +
            "Phí dịch vụ %s cho căn hộ %s sắp đến hạn thanh toán.\n\n" +
            "Chi tiết:\n" +
            "- Loại phí: %s\n" +
            "- Căn hộ: %s\n" +
            "- Số tiền: %,.0f VNĐ\n" +
            "- Hạn thanh toán: %s\n\n" +
            "Vui lòng thanh toán đúng hạn để tránh các khoản phí phát sinh.\n\n" +
            "Trân trọng,\n" +
            "Ban Quản Lý Tòa Nhà",
            residentName, feeTypeName, apartmentCode, feeTypeName, apartmentCode,
            totalAmount, dueDate
        );
    }
    
    /**
     * Xây dựng nội dung email thông báo phí dịch vụ đã đến hạn
     */
    private static String buildServiceFeeDueEmailBody(String residentName, String apartmentCode,
                                                     String feeTypeName, double totalAmount,
                                                     String dueDate) {
        return String.format(
            "Kính gửi ông/bà %s,\n\n" +
            "THÔNG BÁO KHẨN:\n\n" +
            "Phí dịch vụ %s cho căn hộ %s đã đến hạn thanh toán.\n\n" +
            "Chi tiết:\n" +
            "- Loại phí: %s\n" +
            "- Căn hộ: %s\n" +
            "- Số tiền: %,.0f VNĐ\n" +
            "- Hạn thanh toán: %s\n\n" +
            "Vui lòng thanh toán ngay để tránh các khoản phí phát sinh và ảnh hưởng đến dịch vụ.\n\n" +
            "Trân trọng,\n" +
            "Ban Quản Lý Tòa Nhà",
            residentName, feeTypeName, apartmentCode, feeTypeName, apartmentCode,
            totalAmount, dueDate
        );
    }
    
    /**
     * Gửi email
     */
    private static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            // Setup mail properties
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));
            props.put("mail.smtp.auth", String.valueOf(smtpAuth));
            props.put("mail.smtp.starttls.enable", String.valueOf(smtpStartTLS));
            
            // Create session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailUsername, emailPassword);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFromAddress, emailFromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            // Send email
            Transport.send(message);
            
            logger.info("Đã gửi email thành công đến: {}", toEmail);
            return true;
            
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email đến {}: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Kiểm tra xem email service đã sẵn sàng chưa
     */
    public static boolean isEmailServiceReady() {
        return emailEnabled && 
               emailUsername != null && !emailUsername.trim().isEmpty() &&
               emailPassword != null && !emailPassword.trim().isEmpty() &&
               emailFromAddress != null && !emailFromAddress.trim().isEmpty();
    }
}

