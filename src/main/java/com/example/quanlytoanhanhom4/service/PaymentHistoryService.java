package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.PaymentHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho lịch sử thanh toán
 * Module 2: Quản lý phí & công nợ
 */
public class PaymentHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentHistoryService.class);

    public static List<PaymentHistory> getPaymentsByInvoiceId(Integer invoiceId) {
        List<PaymentHistory> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment_history WHERE invoice_id = ? ORDER BY payment_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy lịch sử thanh toán cho hóa đơn ID: {}", invoiceId, e);
        }
        return payments;
    }

    public static boolean addPayment(PaymentHistory payment) {
        String sql = "INSERT INTO payment_history (invoice_id, payment_amount, payment_method, " +
                "payment_reference, processed_by, notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getInvoiceId());
            pstmt.setDouble(2, payment.getPaymentAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentReference());
            pstmt.setObject(5, payment.getProcessedBy());
            pstmt.setString(6, payment.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm lịch sử thanh toán cho hóa đơn ID: {}", payment.getInvoiceId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm lịch sử thanh toán", e);
            return false;
        }
    }

    private static PaymentHistory mapResultSetToPayment(ResultSet rs) throws SQLException {
        PaymentHistory payment = new PaymentHistory();
        payment.setId(rs.getInt("id"));
        payment.setInvoiceId(rs.getInt("invoice_id"));
        payment.setPaymentAmount(rs.getDouble("payment_amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentReference(rs.getString("payment_reference"));
        payment.setNotes(rs.getString("notes"));

        Integer processedBy = rs.getObject("processed_by", Integer.class);
        if (processedBy != null) {
            payment.setProcessedBy(processedBy);
        }

        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }

        return payment;
    }
}



