package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Notification;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    public static List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("🔍 NotificationService: Đang thực thi query: " + sql);
            int count = 0;
            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
                count++;
            }
            System.out.println("✅ NotificationService: Đã lấy được " + count + " thông báo từ database");
        } catch (SQLException e) {
            System.err.println("❌ NotificationService: Lỗi SQL khi lấy danh sách thông báo: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ NotificationService: Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        return notifications;
    }

    public static List<Notification> getNotificationsByStatus(String status) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE status = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static List<Notification> getNotificationsByTarget(String targetType, Integer targetId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE target_type = ? AND (target_id = ? OR target_id IS NULL) ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, targetType);
            if (targetId != null) {
                pstmt.setInt(2, targetId);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Notification notification = mapResultSetToNotification(rs);
                notifications.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public static Notification getNotificationById(int id) {
        String sql = "SELECT * FROM notification WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToNotification(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addNotification(Notification notification) {
        String sql = "INSERT INTO notification (title, content, notification_type, target_type, " +
                "target_id, priority, status, expiry_date, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notification.getTitle());
            pstmt.setString(2, notification.getContent());
            pstmt.setString(3, notification.getNotificationType());
            pstmt.setString(4, notification.getTargetType());
            if (notification.getTargetId() != null) {
                pstmt.setInt(5, notification.getTargetId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setString(6, notification.getPriority() != null ? notification.getPriority() : "NORMAL");
            pstmt.setString(7, notification.getStatus() != null ? notification.getStatus() : "DRAFT");
            if (notification.getExpiryDate() != null) {
                pstmt.setDate(8, Date.valueOf(notification.getExpiryDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            if (notification.getCreatedBy() != null) {
                pstmt.setInt(9, notification.getCreatedBy());
            } else {
                pstmt.setNull(9, Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateNotification(Notification notification) {
        String sql = "UPDATE notification SET title = ?, content = ?, notification_type = ?, " +
                "target_type = ?, target_id = ?, priority = ?, status = ?, sent_date = ?, " +
                "expiry_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notification.getTitle());
            pstmt.setString(2, notification.getContent());
            pstmt.setString(3, notification.getNotificationType());
            pstmt.setString(4, notification.getTargetType());
            if (notification.getTargetId() != null) {
                pstmt.setInt(5, notification.getTargetId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setString(6, notification.getPriority());
            pstmt.setString(7, notification.getStatus());
            if (notification.getSentDate() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(notification.getSentDate()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }
            if (notification.getExpiryDate() != null) {
                pstmt.setDate(9, Date.valueOf(notification.getExpiryDate()));
            } else {
                pstmt.setNull(9, Types.DATE);
            }
            pstmt.setInt(10, notification.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteNotification(int id) {
        String sql = "DELETE FROM notification WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gửi thông báo (đánh dấu là SENT và cập nhật sent_date)
     *
     * @param id ID của thông báo cần gửi
     * @return true nếu thành công, false nếu có lỗi
     */
    public static boolean sendNotification(int id) {
        String sql = "UPDATE notification SET status = 'SENT', sent_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gửi tất cả thông báo có status là DRAFT
     *
     * @return Số lượng thông báo đã gửi thành công
     */
    public static int sendAllDraftNotifications() {
        String sql = "UPDATE notification SET status = 'SENT', sent_date = ? WHERE status = 'DRAFT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Lấy danh sách thông báo có status là DRAFT
     *
     * @return Danh sách thông báo DRAFT
     */
    public static List<Notification> getDraftNotifications() {
        return getNotificationsByStatus("DRAFT");
    }

    private static Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setTitle(rs.getString("title"));
        notification.setContent(rs.getString("content"));
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setTargetType(rs.getString("target_type"));

        int targetId = rs.getInt("target_id");
        if (!rs.wasNull()) {
            notification.setTargetId(targetId);
        }

        notification.setPriority(rs.getString("priority"));
        notification.setStatus(rs.getString("status"));

        Timestamp sentDate = rs.getTimestamp("sent_date");
        if (sentDate != null) {
            notification.setSentDate(sentDate.toLocalDateTime());
        }

        Date expiryDate = rs.getDate("expiry_date");
        if (expiryDate != null) {
            notification.setExpiryDate(expiryDate.toLocalDate());
        }

        int createdBy = rs.getInt("created_by");
        if (!rs.wasNull()) {
            notification.setCreatedBy(createdBy);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            notification.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            notification.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return notification;
    }
}


