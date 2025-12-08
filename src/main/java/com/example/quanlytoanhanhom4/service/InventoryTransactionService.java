package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.InventoryTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho lịch sử nhập xuất kho (Inventory Transaction)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class InventoryTransactionService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryTransactionService.class);

    public static List<InventoryTransaction> getAllTransactions() {
        List<InventoryTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM inventory_transaction ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách giao dịch kho", e);
        }
        return transactions;
    }

    public static List<InventoryTransaction> getTransactionsByResourceId(Integer resourceId) {
        List<InventoryTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM inventory_transaction WHERE resource_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resourceId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy giao dịch theo resource ID: {}", resourceId, e);
        }
        return transactions;
    }

    public static boolean addTransaction(InventoryTransaction transaction) {
        String sql = "INSERT INTO inventory_transaction (transaction_code, resource_id, transaction_type, " +
                "quantity, unit_price, total_amount, reference_type, reference_id, supplier, recipient, " +
                "performed_by, warehouse_name, notes, transaction_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getTransactionCode());
            pstmt.setInt(2, transaction.getResourceId());
            pstmt.setString(3, transaction.getTransactionType());
            pstmt.setInt(4, transaction.getQuantity());
            pstmt.setObject(5, transaction.getUnitPrice());
            pstmt.setObject(6, transaction.getTotalAmount());
            pstmt.setString(7, transaction.getReferenceType());
            pstmt.setObject(8, transaction.getReferenceId());
            pstmt.setString(9, transaction.getSupplier());
            pstmt.setString(10, transaction.getRecipient());
            pstmt.setInt(11, transaction.getPerformedBy());
            pstmt.setString(12, transaction.getWarehouseName());
            pstmt.setString(13, transaction.getNotes());
            pstmt.setTimestamp(14, transaction.getTransactionDate() != null ? 
                    Timestamp.valueOf(transaction.getTransactionDate()) : Timestamp.valueOf(LocalDateTime.now()));
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(15, Timestamp.valueOf(now));
            pstmt.setTimestamp(16, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            
            // Cập nhật số lượng tồn kho
            if (rowsAffected > 0) {
                updateStockQuantity(conn, transaction.getResourceId(), 
                                   transaction.getTransactionType(), transaction.getQuantity());
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm giao dịch kho", e);
            return false;
        }
    }

    public static boolean stockIn(Integer resourceId, Integer quantity, Double unitPrice, 
                                  String supplier, Integer performedBy, String warehouseName) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setResourceId(resourceId);
        transaction.setTransactionType("NHẬP_KHO");
        transaction.setQuantity(quantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setSupplier(supplier);
        transaction.setPerformedBy(performedBy);
        transaction.setWarehouseName(warehouseName);
        return addTransaction(transaction);
    }

    public static boolean stockOut(Integer resourceId, Integer quantity, String referenceType, 
                                  Integer referenceId, String recipient, Integer performedBy, 
                                  String warehouseName) {
        // Kiểm tra số lượng tồn kho
        if (!checkStockAvailability(resourceId, quantity)) {
            logger.warn("Không đủ số lượng tồn kho cho resource ID: {}", resourceId);
            return false;
        }

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setResourceId(resourceId);
        transaction.setTransactionType("XUẤT_KHO");
        transaction.setQuantity(quantity);
        transaction.setReferenceType(referenceType);
        transaction.setReferenceId(referenceId);
        transaction.setRecipient(recipient);
        transaction.setPerformedBy(performedBy);
        transaction.setWarehouseName(warehouseName);
        return addTransaction(transaction);
    }

    private static boolean checkStockAvailability(Integer resourceId, Integer quantity) {
        String sql = "SELECT quantity_in_stock FROM maintenance_resource WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resourceId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int stock = rs.getInt("quantity_in_stock");
                return stock >= quantity;
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi kiểm tra tồn kho", e);
        }
        return false;
    }

    private static void updateStockQuantity(Connection conn, Integer resourceId, 
                                           String transactionType, Integer quantity) throws SQLException {
        String sql;
        if ("NHẬP_KHO".equals(transactionType)) {
            sql = "UPDATE maintenance_resource SET quantity_in_stock = quantity_in_stock + ? WHERE id = ?";
        } else if ("XUẤT_KHO".equals(transactionType)) {
            sql = "UPDATE maintenance_resource SET quantity_in_stock = quantity_in_stock - ? WHERE id = ?";
        } else {
            return; // Kiểm kho không thay đổi số lượng
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, resourceId);
            pstmt.executeUpdate();
        }
    }

    private static InventoryTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(rs.getInt("id"));
        transaction.setTransactionCode(rs.getString("transaction_code"));
        transaction.setResourceId(rs.getInt("resource_id"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setQuantity(rs.getInt("quantity"));
        transaction.setUnitPrice(rs.getObject("unit_price", Double.class));
        transaction.setTotalAmount(rs.getObject("total_amount", Double.class));
        transaction.setReferenceType(rs.getString("reference_type"));
        transaction.setReferenceId(rs.getObject("reference_id", Integer.class));
        transaction.setSupplier(rs.getString("supplier"));
        transaction.setRecipient(rs.getString("recipient"));
        transaction.setPerformedBy(rs.getInt("performed_by"));
        transaction.setWarehouseName(rs.getString("warehouse_name"));
        transaction.setNotes(rs.getString("notes"));
        
        Timestamp transactionDate = rs.getTimestamp("transaction_date");
        transaction.setTransactionDate(transactionDate != null ? transactionDate.toLocalDateTime() : null);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        transaction.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        transaction.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return transaction;
    }
}

