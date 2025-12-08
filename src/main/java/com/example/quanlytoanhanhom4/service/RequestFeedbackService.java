package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.RequestFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho phản hồi và đánh giá của cư dân (Resident Feedback)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestFeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(RequestFeedbackService.class);

    public static RequestFeedback getFeedbackByTicketId(Integer ticketId) {
        String sql = "SELECT * FROM request_feedback WHERE ticket_id = ? ORDER BY feedback_date DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFeedback(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy phản hồi cho ticket ID: {}", ticketId, e);
        }
        return null;
    }

    public static List<RequestFeedback> getAllFeedbacks() {
        List<RequestFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM request_feedback ORDER BY feedback_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                feedbacks.add(mapResultSetToFeedback(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách phản hồi", e);
        }
        return feedbacks;
    }

    public static List<RequestFeedback> getFeedbacksByRating(Integer minRating) {
        List<RequestFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM request_feedback WHERE satisfaction_rating >= ? ORDER BY feedback_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minRating);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                feedbacks.add(mapResultSetToFeedback(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy phản hồi theo rating: {}", minRating, e);
        }
        return feedbacks;
    }

    public static boolean addFeedback(RequestFeedback feedback) {
        String sql = "INSERT INTO request_feedback (ticket_id, resident_id, satisfaction_rating, " +
                "detailed_comment, feedback_image_path, wants_reopen, reopen_reason, feedback_date, " +
                "created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, feedback.getTicketId());
            pstmt.setInt(2, feedback.getResidentId());
            pstmt.setInt(3, feedback.getSatisfactionRating());
            pstmt.setString(4, feedback.getDetailedComment());
            pstmt.setString(5, feedback.getFeedbackImagePath());
            pstmt.setBoolean(6, feedback.getWantsReopen() != null ? feedback.getWantsReopen() : false);
            pstmt.setString(7, feedback.getReopenReason());
            pstmt.setTimestamp(8, feedback.getFeedbackDate() != null ? 
                    Timestamp.valueOf(feedback.getFeedbackDate()) : Timestamp.valueOf(LocalDateTime.now()));
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(9, Timestamp.valueOf(now));
            pstmt.setTimestamp(10, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật wants_reopen trong ticket nếu cư dân muốn mở lại
                if (feedback.getWantsReopen() != null && feedback.getWantsReopen()) {
                    updateTicketWantsReopen(feedback.getTicketId(), true, feedback.getReopenReason());
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm phản hồi", e);
            return false;
        }
    }

    public static boolean updateFeedback(RequestFeedback feedback) {
        String sql = "UPDATE request_feedback SET satisfaction_rating = ?, detailed_comment = ?, " +
                "feedback_image_path = ?, wants_reopen = ?, reopen_reason = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, feedback.getSatisfactionRating());
            pstmt.setString(2, feedback.getDetailedComment());
            pstmt.setString(3, feedback.getFeedbackImagePath());
            pstmt.setBoolean(4, feedback.getWantsReopen() != null ? feedback.getWantsReopen() : false);
            pstmt.setString(5, feedback.getReopenReason());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(7, feedback.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật phản hồi", e);
            return false;
        }
    }

    private static void updateTicketWantsReopen(Integer ticketId, Boolean wantsReopen, String reason) {
        String sql = "UPDATE ticket SET wants_reopen = ?, status = 'TIẾP_NHẬN', updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, wantsReopen);
            pstmt.setInt(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật wants_reopen cho ticket", e);
        }
    }

    private static RequestFeedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        RequestFeedback feedback = new RequestFeedback();
        feedback.setId(rs.getInt("id"));
        feedback.setTicketId(rs.getInt("ticket_id"));
        feedback.setResidentId(rs.getInt("resident_id"));
        feedback.setSatisfactionRating(rs.getObject("satisfaction_rating", Integer.class));
        feedback.setDetailedComment(rs.getString("detailed_comment"));
        feedback.setFeedbackImagePath(rs.getString("feedback_image_path"));
        feedback.setWantsReopen(rs.getBoolean("wants_reopen"));
        feedback.setReopenReason(rs.getString("reopen_reason"));
        
        Timestamp feedbackDate = rs.getTimestamp("feedback_date");
        feedback.setFeedbackDate(feedbackDate != null ? feedbackDate.toLocalDateTime() : null);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        feedback.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        feedback.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return feedback;
    }
}

