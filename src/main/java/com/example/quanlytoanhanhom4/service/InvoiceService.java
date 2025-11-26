package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Invoice;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {

    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice ORDER BY invoice_date DESC, invoice_number";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("🔍 InvoiceService: Đang thực thi query: " + sql);
            int count = 0;
            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
                count++;
            }
            System.out.println("✅ InvoiceService: Đã lấy được " + count + " hóa đơn từ database");
        } catch (SQLException e) {
            System.err.println("❌ InvoiceService: Lỗi SQL khi lấy danh sách hóa đơn: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ InvoiceService: Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        return invoices;
    }

    public static List<Invoice> getInvoicesByApartment(int apartmentId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE apartment_id = ? ORDER BY invoice_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public static List<Invoice> getInvoicesByStatus(String status) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE status = ? ORDER BY invoice_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public static Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToInvoice(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (apartment_id, invoice_number, invoice_date, due_date, " +
                "total_amount, paid_amount, remaining_amount, status, created_by, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getApartmentId());
            pstmt.setString(2, invoice.getInvoiceNumber());
            pstmt.setDate(3, Date.valueOf(invoice.getInvoiceDate()));
            pstmt.setDate(4, Date.valueOf(invoice.getDueDate()));
            pstmt.setDouble(5, invoice.getTotalAmount());
            pstmt.setDouble(6, invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0.0);
            pstmt.setDouble(7, invoice.getRemainingAmount() != null ? invoice.getRemainingAmount() : invoice.getTotalAmount());
            pstmt.setString(8, invoice.getStatus() != null ? invoice.getStatus() : "CHỜ_THANH_TOÁN");
            if (invoice.getCreatedBy() != null) {
                pstmt.setInt(9, invoice.getCreatedBy());
            } else {
                pstmt.setNull(9, Types.INTEGER);
            }
            pstmt.setString(10, invoice.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoice SET apartment_id = ?, invoice_number = ?, invoice_date = ?, " +
                "due_date = ?, total_amount = ?, paid_amount = ?, remaining_amount = ?, " +
                "status = ?, payment_method = ?, payment_date = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getApartmentId());
            pstmt.setString(2, invoice.getInvoiceNumber());
            pstmt.setDate(3, Date.valueOf(invoice.getInvoiceDate()));
            pstmt.setDate(4, Date.valueOf(invoice.getDueDate()));
            pstmt.setDouble(5, invoice.getTotalAmount());
            pstmt.setDouble(6, invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0.0);
            pstmt.setDouble(7, invoice.getRemainingAmount() != null ? invoice.getRemainingAmount() : invoice.getTotalAmount());
            pstmt.setString(8, invoice.getStatus());
            pstmt.setString(9, invoice.getPaymentMethod());
            if (invoice.getPaymentDate() != null) {
                pstmt.setDate(10, Date.valueOf(invoice.getPaymentDate()));
            } else {
                pstmt.setNull(10, Types.DATE);
            }
            pstmt.setString(11, invoice.getNotes());
            pstmt.setInt(12, invoice.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteInvoice(int id) {
        String sql = "DELETE FROM invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setApartmentId(rs.getInt("apartment_id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));

        Date invoiceDate = rs.getDate("invoice_date");
        if (invoiceDate != null) {
            invoice.setInvoiceDate(invoiceDate.toLocalDate());
        }

        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            invoice.setDueDate(dueDate.toLocalDate());
        }

        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setPaidAmount(rs.getDouble("paid_amount"));
        invoice.setRemainingAmount(rs.getDouble("remaining_amount"));
        invoice.setStatus(rs.getString("status"));
        invoice.setPaymentMethod(rs.getString("payment_method"));

        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            invoice.setPaymentDate(paymentDate.toLocalDate());
        }

        invoice.setNotes(rs.getString("notes"));

        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            invoice.setCreatedBy(createdBy);
        }

        // Kiểm tra và đọc created_at nếu có
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                invoice.setCreatedAt(createdAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        // Kiểm tra và đọc updated_at nếu có
        try {
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                invoice.setUpdatedAt(updatedAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        return invoice;
    }
}


