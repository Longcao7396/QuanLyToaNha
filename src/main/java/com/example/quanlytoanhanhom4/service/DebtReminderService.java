package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.model.DebtRecord;
import com.example.quanlytoanhanhom4.model.Invoice;
import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.model.Resident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Service nhắc nợ tự động
 * Tính năng thực tiễn: Tự động gửi thông báo nhắc nợ cho cư dân
 */
public class DebtReminderService {
    private static final Logger logger = LoggerFactory.getLogger(DebtReminderService.class);

    /**
     * Gửi thông báo nhắc nợ cho tất cả căn hộ có công nợ
     */
    public static int sendDebtReminders() {
        int count = 0;
        List<DebtRecord> debts = DebtRecordService.getAllDebts();
        
        for (DebtRecord debt : debts) {
            if (debt.getTotalDebt() > 0) {
                if (sendReminderForApartment(debt.getApartmentId(), debt.getTotalDebt())) {
                    count++;
                }
            }
        }
        
        logger.info("Đã gửi {} thông báo nhắc nợ", count);
        return count;
    }

    /**
     * Gửi thông báo nhắc nợ cho một căn hộ
     */
    private static boolean sendReminderForApartment(Integer apartmentId, Double totalDebt) {
        // Lấy thông tin cư dân chủ hộ
        Resident owner = ResidentService.getResidentByApartmentId(apartmentId);
        if (owner == null) {
            logger.warn("Không tìm thấy chủ hộ cho căn hộ {}", apartmentId);
            return false;
        }

        // Tạo thông báo nhắc nợ
        Notification notification = new Notification();
        notification.setNotificationType("DEBT_REMINDER");
        notification.setTitle("Nhắc nợ - Căn hộ " + ApartmentService.getApartmentById(apartmentId).getApartmentNo());
        notification.setContent(String.format(
            "Kính gửi Quý cư dân,\n\n" +
            "Căn hộ của Quý cư dân hiện có công nợ: %,.0f VNĐ\n\n" +
            "Vui lòng thanh toán sớm để tránh phát sinh lãi suất.\n\n" +
            "Trân trọng,\nBan Quản Lý Tòa Nhà",
            totalDebt
        ));
        notification.setTargetType("RESIDENT");
        notification.setTargetId(owner.getId());
        notification.setPriority("HIGH");
        notification.setStatus("SENT");
        notification.setSentDate(java.time.LocalDateTime.now());

        if (NotificationService.addNotification(notification)) {
            logger.info("Đã gửi thông báo nhắc nợ cho căn hộ {}", apartmentId);
            return true;
        }

        return false;
    }

    /**
     * Gửi thông báo nhắc nợ cho các hóa đơn sắp đến hạn
     */
    public static int sendUpcomingDueDateReminders(int daysBeforeDue) {
        int count = 0;
        LocalDate reminderDate = LocalDate.now().plusDays(daysBeforeDue);
        
        List<Invoice> invoices = InvoiceService.getInvoicesByStatus("PENDING");
        
        for (Invoice invoice : invoices) {
            if (invoice.getDueDate() != null && invoice.getDueDate().equals(reminderDate)) {
                if (sendReminderForInvoice(invoice)) {
                    count++;
                }
            }
        }
        
        logger.info("Đã gửi {} thông báo nhắc hạn thanh toán", count);
        return count;
    }

    /**
     * Gửi thông báo nhắc hạn thanh toán cho một hóa đơn
     */
    private static boolean sendReminderForInvoice(Invoice invoice) {
        Resident owner = ResidentService.getResidentByApartmentId(invoice.getApartmentId());
        if (owner == null) {
            return false;
        }

        Notification notification = new Notification();
        notification.setNotificationType("BILLING");
        notification.setTitle("Nhắc hạn thanh toán - Hóa đơn " + invoice.getInvoiceNumber());
        notification.setContent(String.format(
            "Kính gửi Quý cư dân,\n\n" +
            "Hóa đơn %s có hạn thanh toán vào ngày %s.\n" +
            "Số tiền: %,.0f VNĐ\n\n" +
            "Vui lòng thanh toán đúng hạn.\n\n" +
            "Trân trọng,\nBan Quản Lý Tòa Nhà",
            invoice.getInvoiceNumber(),
            invoice.getDueDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            invoice.getTotalAmount()
        ));
        notification.setTargetType("RESIDENT");
        notification.setTargetId(owner.getId());
        notification.setPriority("NORMAL");
        notification.setStatus("SENT");
        notification.setSentDate(java.time.LocalDateTime.now());

        return NotificationService.addNotification(notification);
    }

    /**
     * Tự động cập nhật công nợ cho tất cả căn hộ
     */
    public static void updateAllDebts() {
        List<Apartment> apartments = ApartmentService.getAllApartments();
        
        for (Apartment apartment : apartments) {
            DebtRecordService.calculateDebtForApartment(apartment.getId());
        }
        
        logger.info("Đã cập nhật công nợ cho {} căn hộ", apartments.size());
    }
}

