package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.RentalContract;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý hợp đồng thuê
 * Module 1: Quản lý cư dân & căn hộ
 * 4. Quản lý hợp đồng thuê (nếu căn hộ cho thuê)
 */
public class RentalContractService {

    public static List<RentalContract> getAllRentalContracts() {
        List<RentalContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM rental_contract ORDER BY start_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RentalContract contract = mapResultSetToRentalContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách hợp đồng thuê: " + e.getMessage());
            e.printStackTrace();
        }
        return contracts;
    }

    public static RentalContract getRentalContractById(int id) {
        String sql = "SELECT * FROM rental_contract WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRentalContract(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hợp đồng với ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public static List<RentalContract> getRentalContractsByApartmentId(int apartmentId) {
        List<RentalContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM rental_contract WHERE apartment_id = ? ORDER BY start_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalContract contract = mapResultSetToRentalContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hợp đồng theo căn hộ ID: " + apartmentId);
            e.printStackTrace();
        }
        return contracts;
    }

    public static List<RentalContract> getRentalContractsByOwnerId(int ownerId) {
        List<RentalContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM rental_contract WHERE owner_id = ? ORDER BY start_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalContract contract = mapResultSetToRentalContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hợp đồng theo chủ hộ ID: " + ownerId);
            e.printStackTrace();
        }
        return contracts;
    }

    public static List<RentalContract> getRentalContractsByStatus(String status) {
        List<RentalContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM rental_contract WHERE status = ? ORDER BY start_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RentalContract contract = mapResultSetToRentalContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hợp đồng theo trạng thái: " + status);
            e.printStackTrace();
        }
        return contracts;
    }

    /**
     * Lấy danh sách hợp đồng sắp hết hạn (trong vòng 30 ngày)
     */
    public static List<RentalContract> getExpiringContracts() {
        List<RentalContract> contracts = new ArrayList<>();
        String sql = "SELECT * FROM rental_contract WHERE status = 'ACTIVE' " +
                     "AND end_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                     "AND end_date >= CURDATE() " +
                     "ORDER BY end_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RentalContract contract = mapResultSetToRentalContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy hợp đồng sắp hết hạn: " + e.getMessage());
            e.printStackTrace();
        }
        return contracts;
    }

    public static boolean addRentalContract(RentalContract contract) {
        String sql = "INSERT INTO rental_contract (apartment_id, owner_id, resident_id, " +
                     "contract_number, start_date, end_date, monthly_rent, deposit, " +
                     "contract_file_path, special_terms, renewal_history, status, notes, " +
                     "created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, contract.getApartmentId());
            pstmt.setObject(2, contract.getOwnerId());
            pstmt.setInt(3, contract.getResidentId());
            pstmt.setString(4, contract.getContractNumber());
            pstmt.setDate(5, contract.getStartDate() != null ? 
                Date.valueOf(contract.getStartDate()) : null);
            pstmt.setDate(6, contract.getEndDate() != null ? 
                Date.valueOf(contract.getEndDate()) : null);
            pstmt.setObject(7, contract.getMonthlyRent());
            pstmt.setObject(8, contract.getDeposit());
            pstmt.setString(9, contract.getContractFilePath());
            pstmt.setString(10, contract.getSpecialTerms());
            pstmt.setString(11, contract.getRenewalHistory());
            pstmt.setString(12, contract.getStatus() != null ? contract.getStatus() : "ACTIVE");
            pstmt.setString(13, contract.getNotes());
            LocalDate now = LocalDate.now();
            pstmt.setDate(14, Date.valueOf(now));
            pstmt.setDate(15, Date.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    contract.setId(rs.getInt(1));
                }
                System.out.println("Đã tạo hợp đồng thuê mới: " + contract.getContractNumber());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo hợp đồng thuê: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateRentalContract(RentalContract contract) {
        String sql = "UPDATE rental_contract SET apartment_id = ?, owner_id = ?, resident_id = ?, " +
                     "contract_number = ?, start_date = ?, end_date = ?, monthly_rent = ?, " +
                     "deposit = ?, contract_file_path = ?, special_terms = ?, renewal_history = ?, " +
                     "status = ?, notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getApartmentId());
            pstmt.setObject(2, contract.getOwnerId());
            pstmt.setInt(3, contract.getResidentId());
            pstmt.setString(4, contract.getContractNumber());
            pstmt.setDate(5, contract.getStartDate() != null ? 
                Date.valueOf(contract.getStartDate()) : null);
            pstmt.setDate(6, contract.getEndDate() != null ? 
                Date.valueOf(contract.getEndDate()) : null);
            pstmt.setObject(7, contract.getMonthlyRent());
            pstmt.setObject(8, contract.getDeposit());
            pstmt.setString(9, contract.getContractFilePath());
            pstmt.setString(10, contract.getSpecialTerms());
            pstmt.setString(11, contract.getRenewalHistory());
            pstmt.setString(12, contract.getStatus());
            pstmt.setString(13, contract.getNotes());
            pstmt.setDate(14, Date.valueOf(LocalDate.now()));
            pstmt.setInt(15, contract.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã cập nhật hợp đồng ID " + contract.getId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật hợp đồng: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gia hạn hợp đồng
     */
    public static boolean renewRentalContract(int contractId, LocalDate newEndDate, String renewalNote) {
        RentalContract contract = getRentalContractById(contractId);
        if (contract == null) {
            return false;
        }

        // Cập nhật lịch sử gia hạn
        String renewalHistory = contract.getRenewalHistory();
        if (renewalHistory == null) {
            renewalHistory = "";
        }
        renewalHistory += String.format("\nGia hạn: %s -> %s. Ghi chú: %s", 
            contract.getEndDate(), newEndDate, renewalNote);

        contract.setEndDate(newEndDate);
        contract.setRenewalHistory(renewalHistory);
        contract.setStatus("ACTIVE");

        return updateRentalContract(contract);
    }

    /**
     * Kết thúc hợp đồng
     */
    public static boolean endRentalContract(int contractId, String reason) {
        RentalContract contract = getRentalContractById(contractId);
        if (contract == null) {
            return false;
        }

        contract.setStatus("TERMINATED");
        if (reason != null && !reason.isEmpty()) {
            contract.setNotes((contract.getNotes() != null ? contract.getNotes() + "\n" : "") + 
                "Kết thúc: " + reason);
        }

        return updateRentalContract(contract);
    }

    public static boolean deleteRentalContract(int id) {
        String sql = "DELETE FROM rental_contract WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa hợp đồng với ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa hợp đồng với ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    private static RentalContract mapResultSetToRentalContract(ResultSet rs) throws SQLException {
        RentalContract contract = new RentalContract();
        contract.setId(rs.getInt("id"));
        contract.setApartmentId(rs.getInt("apartment_id"));
        
        // Đọc owner_id nếu có
        try {
            Integer ownerId = rs.getObject("owner_id", Integer.class);
            contract.setOwnerId(ownerId);
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
            contract.setOwnerId(null);
        }
        
        contract.setResidentId(rs.getInt("resident_id"));
        contract.setContractNumber(rs.getString("contract_number"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            contract.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            contract.setEndDate(endDate.toLocalDate());
        }
        
        Double monthlyRent = rs.getObject("monthly_rent", Double.class);
        contract.setMonthlyRent(monthlyRent);
        
        Double deposit = rs.getObject("deposit", Double.class);
        contract.setDeposit(deposit);
        
        // Đọc các trường mới
        try {
            contract.setContractFilePath(rs.getString("contract_file_path"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        try {
            contract.setSpecialTerms(rs.getString("special_terms"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        try {
            contract.setRenewalHistory(rs.getString("renewal_history"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        contract.setStatus(rs.getString("status"));
        contract.setNotes(rs.getString("notes"));
        
        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            contract.setCreatedAt(createdAt.toLocalDate());
        }
        
        Date updatedAt = rs.getDate("updated_at");
        if (updatedAt != null) {
            contract.setUpdatedAt(updatedAt.toLocalDate());
        }

        return contract;
    }
}

