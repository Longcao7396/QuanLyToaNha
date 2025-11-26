package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.RepairRequest;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepairRequestService {

    public static List<RepairRequest> getAllRepairRequests() {
        List<RepairRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM repair_request ORDER BY requested_date DESC, priority DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("🔍 RepairRequestService: Đang thực thi query: " + sql);
            int count = 0;
            while (rs.next()) {
                RepairRequest request = mapResultSetToRepairRequest(rs);
                requests.add(request);
                count++;
            }
            System.out.println("✅ RepairRequestService: Đã lấy được " + count + " yêu cầu sửa chữa từ database");
        } catch (SQLException e) {
            System.err.println("❌ RepairRequestService: Lỗi SQL khi lấy danh sách yêu cầu sửa chữa: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ RepairRequestService: Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        return requests;
    }

    public static List<RepairRequest> getRepairRequestsByStatus(String status) {
        List<RepairRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM repair_request WHERE status = ? ORDER BY requested_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RepairRequest request = mapResultSetToRepairRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static List<RepairRequest> getRepairRequestsByApartment(int apartmentId) {
        List<RepairRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM repair_request WHERE apartment_id = ? ORDER BY requested_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RepairRequest request = mapResultSetToRepairRequest(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static RepairRequest getRepairRequestById(int id) {
        String sql = "SELECT * FROM repair_request WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRepairRequest(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addRepairRequest(RepairRequest request) {
        String sql = "INSERT INTO repair_request (apartment_id, resident_id, title, description, " +
                "repair_type, priority, status, requested_date, scheduled_date, assigned_to, " +
                "estimated_cost, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getApartmentId());
            pstmt.setInt(2, request.getResidentId());
            pstmt.setString(3, request.getTitle());
            pstmt.setString(4, request.getDescription());
            pstmt.setString(5, request.getRepairType());
            pstmt.setString(6, request.getPriority() != null ? request.getPriority() : "TRUNG_BÌNH");
            pstmt.setString(7, request.getStatus() != null ? request.getStatus() : "CHỜ_XỬ_LÝ");
            pstmt.setDate(8, Date.valueOf(request.getRequestedDate()));
            if (request.getScheduledDate() != null) {
                pstmt.setDate(9, Date.valueOf(request.getScheduledDate()));
            } else {
                pstmt.setNull(9, Types.DATE);
            }
            if (request.getAssignedTo() != null) {
                pstmt.setInt(10, request.getAssignedTo());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }
            if (request.getEstimatedCost() != null) {
                pstmt.setDouble(11, request.getEstimatedCost());
            } else {
                pstmt.setNull(11, Types.DOUBLE);
            }
            pstmt.setString(12, request.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRepairRequest(RepairRequest request) {
        String sql = "UPDATE repair_request SET apartment_id = ?, resident_id = ?, title = ?, " +
                "description = ?, repair_type = ?, priority = ?, status = ?, requested_date = ?, " +
                "scheduled_date = ?, completed_date = ?, assigned_to = ?, estimated_cost = ?, " +
                "actual_cost = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, request.getApartmentId());
            pstmt.setInt(2, request.getResidentId());
            pstmt.setString(3, request.getTitle());
            pstmt.setString(4, request.getDescription());
            pstmt.setString(5, request.getRepairType());
            pstmt.setString(6, request.getPriority());
            pstmt.setString(7, request.getStatus());
            pstmt.setDate(8, Date.valueOf(request.getRequestedDate()));
            if (request.getScheduledDate() != null) {
                pstmt.setDate(9, Date.valueOf(request.getScheduledDate()));
            } else {
                pstmt.setNull(9, Types.DATE);
            }
            if (request.getCompletedDate() != null) {
                pstmt.setDate(10, Date.valueOf(request.getCompletedDate()));
            } else {
                pstmt.setNull(10, Types.DATE);
            }
            if (request.getAssignedTo() != null) {
                pstmt.setInt(11, request.getAssignedTo());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }
            if (request.getEstimatedCost() != null) {
                pstmt.setDouble(12, request.getEstimatedCost());
            } else {
                pstmt.setNull(12, Types.DOUBLE);
            }
            if (request.getActualCost() != null) {
                pstmt.setDouble(13, request.getActualCost());
            } else {
                pstmt.setNull(13, Types.DOUBLE);
            }
            pstmt.setString(14, request.getNotes());
            pstmt.setInt(15, request.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRepairRequest(int id) {
        String sql = "DELETE FROM repair_request WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static RepairRequest mapResultSetToRepairRequest(ResultSet rs) throws SQLException {
        RepairRequest request = new RepairRequest();
        request.setId(rs.getInt("id"));
        request.setApartmentId(rs.getInt("apartment_id"));
        request.setResidentId(rs.getInt("resident_id"));
        request.setTitle(rs.getString("title"));
        request.setDescription(rs.getString("description"));
        request.setRepairType(rs.getString("repair_type"));
        request.setPriority(rs.getString("priority"));
        request.setStatus(rs.getString("status"));

        Date requestedDate = rs.getDate("requested_date");
        if (requestedDate != null) {
            request.setRequestedDate(requestedDate.toLocalDate());
        }

        Date scheduledDate = rs.getDate("scheduled_date");
        if (scheduledDate != null) {
            request.setScheduledDate(scheduledDate.toLocalDate());
        }

        Date completedDate = rs.getDate("completed_date");
        if (completedDate != null) {
            request.setCompletedDate(completedDate.toLocalDate());
        }

        int assignedTo = rs.getInt("assigned_to");
        if (!rs.wasNull()) {
            request.setAssignedTo(assignedTo);
        }

        double estimatedCost = rs.getDouble("estimated_cost");
        if (!rs.wasNull()) {
            request.setEstimatedCost(estimatedCost);
        }

        double actualCost = rs.getDouble("actual_cost");
        if (!rs.wasNull()) {
            request.setActualCost(actualCost);
        }

        request.setNotes(rs.getString("notes"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            request.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            request.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return request;
    }
}


