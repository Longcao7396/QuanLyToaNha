package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Contract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContractService {

    public static List<Contract> getAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = """
                SELECT c.*, s.full_name
                FROM contract c
                JOIN staff s ON s.id = c.staff_id
                ORDER BY c.start_date DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                contracts.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contracts;
    }

    public static boolean addContract(Contract contract) {
        String sql = "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getStaffId());
            pstmt.setString(2, contract.getContractType());
            pstmt.setObject(3, contract.getStartDate());
            pstmt.setObject(4, contract.getEndDate());
            pstmt.setObject(5, contract.getSalary());
            pstmt.setString(6, contract.getStatus());
            pstmt.setString(7, contract.getDescription());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateContract(Contract contract) {
        String sql = "UPDATE contract SET staff_id = ?, contract_type = ?, start_date = ?, end_date = ?, salary = ?, status = ?, description = ? "
                   + "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, contract.getStaffId());
            pstmt.setString(2, contract.getContractType());
            pstmt.setObject(3, contract.getStartDate());
            pstmt.setObject(4, contract.getEndDate());
            pstmt.setObject(5, contract.getSalary());
            pstmt.setString(6, contract.getStatus());
            pstmt.setString(7, contract.getDescription());
            pstmt.setInt(8, contract.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteContract(int id) {
        String sql = "DELETE FROM contract WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Contract mapResultSet(ResultSet rs) throws SQLException {
        Contract contract = new Contract();
        contract.setId(rs.getInt("id"));
        contract.setStaffId(rs.getInt("staff_id"));
        contract.setStaffName(rs.getString("full_name"));
        contract.setContractType(rs.getString("contract_type"));

        java.sql.Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            contract.setStartDate(startDate.toLocalDate());
        }
        java.sql.Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            contract.setEndDate(endDate.toLocalDate());
        }

        contract.setSalary(rs.getObject("salary", Double.class));
        contract.setStatus(rs.getString("status"));
        contract.setDescription(rs.getString("description"));
        return contract;
    }
}


