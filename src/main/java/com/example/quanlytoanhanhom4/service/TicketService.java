package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Ticket;
import com.example.quanlytoanhanhom4.util.DataStructureUtils;
import com.example.quanlytoanhanhom4.util.SearchUtils;
import com.example.quanlytoanhanhom4.util.SortingUtils;
import com.example.quanlytoanhanhom4.util.comparator.TicketComparators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service cho quản lý ticket/yêu cầu & sự cố
 * Module 5: Quản lý yêu cầu & sự cố (Ticket System)
 */
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    public static List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách ticket", e);
        }
        return tickets;
    }

    public static List<Ticket> getTicketsByResidentId(Integer residentId) {
        if (residentId == null || residentId <= 0) {
            logger.warn("Resident ID không hợp lệ: {}", residentId);
            return new ArrayList<>();
        }
        
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE resident_id = ? ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
            logger.debug("Đã lấy {} ticket cho cư dân ID: {}", tickets.size(), residentId);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket của cư dân ID: {}", residentId, e);
        }
        return tickets;
    }

    public static List<Ticket> getTicketsByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            logger.warn("Status không được null hoặc rỗng");
            return new ArrayList<>();
        }
        
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status = ? ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
            logger.debug("Đã lấy {} ticket với status: {}", tickets.size(), status);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket theo trạng thái: {}", status, e);
        }
        return tickets;
    }

    public static Ticket getTicketById(Integer id) {
        if (id == null || id <= 0) {
            logger.warn("Ticket ID không hợp lệ: {}", id);
            return null;
        }
        
        String sql = "SELECT * FROM ticket WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicket(rs);
                }
            }
            logger.debug("Không tìm thấy ticket với ID: {}", id);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket ID: {}", id, e);
        }
        return null;
    }

    public static boolean addTicket(Ticket ticket) {
        String sql = "INSERT INTO ticket (ticket_number, apartment_id, resident_id, ticket_type, category, " +
                "title, description, attachment_path, location, priority, status, submission_channel, department, " +
                "sla_hours, sla_deadline, received_by, received_date, assigned_to, assigned_date, start_time, " +
                "internal_notes, rejection_reason, wants_reopen, created_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTicketNumber());
            pstmt.setObject(2, ticket.getApartmentId());
            pstmt.setInt(3, ticket.getResidentId());
            pstmt.setString(4, ticket.getTicketType());
            pstmt.setString(5, ticket.getCategory());
            pstmt.setString(6, ticket.getTitle());
            pstmt.setString(7, ticket.getDescription());
            pstmt.setString(8, ticket.getAttachmentPath());
            pstmt.setString(9, ticket.getLocation());
            pstmt.setString(10, ticket.getPriority() != null ? ticket.getPriority() : "TRUNG_BÌNH");
            pstmt.setString(11, ticket.getStatus() != null ? ticket.getStatus() : "TIẾP_NHẬN");
            pstmt.setString(12, ticket.getSubmissionChannel());
            pstmt.setString(13, ticket.getDepartment());
            pstmt.setObject(14, ticket.getSlaHours());
            pstmt.setTimestamp(15, ticket.getSlaDeadline() != null ? 
                    Timestamp.valueOf(ticket.getSlaDeadline()) : null);
            pstmt.setObject(16, ticket.getReceivedBy());
            pstmt.setTimestamp(17, ticket.getReceivedDate() != null ? 
                    Timestamp.valueOf(ticket.getReceivedDate()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setObject(18, ticket.getAssignedTo());
            pstmt.setTimestamp(19, ticket.getAssignedDate() != null ? 
                    Timestamp.valueOf(ticket.getAssignedDate()) : null);
            pstmt.setTimestamp(20, ticket.getStartTime() != null ? 
                    Timestamp.valueOf(ticket.getStartTime()) : null);
            pstmt.setString(21, ticket.getInternalNotes());
            pstmt.setString(22, ticket.getRejectionReason());
            pstmt.setObject(23, ticket.getWantsReopen());
            pstmt.setTimestamp(24, ticket.getCreatedDate() != null ? 
                    Timestamp.valueOf(ticket.getCreatedDate()) : Timestamp.valueOf(LocalDateTime.now()));
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(25, Timestamp.valueOf(now));
            pstmt.setTimestamp(26, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm ticket: {}", ticket.getTicketNumber());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm ticket", e);
            return false;
        }
    }

    public static boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE ticket SET ticket_type = ?, category = ?, title = ?, description = ?, " +
                "attachment_path = ?, location = ?, priority = ?, status = ?, submission_channel = ?, department = ?, " +
                "sla_hours = ?, sla_deadline = ?, received_by = ?, received_date = ?, assigned_to = ?, " +
                "assigned_date = ?, start_time = ?, resolved_date = ?, closed_date = ?, estimated_cost = ?, " +
                "actual_cost = ?, resolution = ?, internal_notes = ?, rejection_reason = ?, wants_reopen = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTicketType());
            pstmt.setString(2, ticket.getCategory());
            pstmt.setString(3, ticket.getTitle());
            pstmt.setString(4, ticket.getDescription());
            pstmt.setString(5, ticket.getAttachmentPath());
            pstmt.setString(6, ticket.getLocation());
            pstmt.setString(7, ticket.getPriority());
            pstmt.setString(8, ticket.getStatus());
            pstmt.setString(9, ticket.getSubmissionChannel());
            pstmt.setString(10, ticket.getDepartment());
            pstmt.setObject(11, ticket.getSlaHours());
            pstmt.setTimestamp(12, ticket.getSlaDeadline() != null ? 
                    Timestamp.valueOf(ticket.getSlaDeadline()) : null);
            pstmt.setObject(13, ticket.getReceivedBy());
            pstmt.setTimestamp(14, ticket.getReceivedDate() != null ? 
                    Timestamp.valueOf(ticket.getReceivedDate()) : null);
            pstmt.setObject(15, ticket.getAssignedTo());
            pstmt.setTimestamp(16, ticket.getAssignedDate() != null ? 
                    Timestamp.valueOf(ticket.getAssignedDate()) : null);
            pstmt.setTimestamp(17, ticket.getStartTime() != null ? 
                    Timestamp.valueOf(ticket.getStartTime()) : null);
            pstmt.setTimestamp(18, ticket.getResolvedDate() != null ? 
                    Timestamp.valueOf(ticket.getResolvedDate()) : null);
            pstmt.setTimestamp(19, ticket.getClosedDate() != null ? 
                    Timestamp.valueOf(ticket.getClosedDate()) : null);
            pstmt.setObject(20, ticket.getEstimatedCost());
            pstmt.setObject(21, ticket.getActualCost());
            pstmt.setString(22, ticket.getResolution());
            pstmt.setString(23, ticket.getInternalNotes());
            pstmt.setString(24, ticket.getRejectionReason());
            pstmt.setObject(25, ticket.getWantsReopen());
            pstmt.setTimestamp(26, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(27, ticket.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật ticket ID: {}", ticket.getId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật ticket", e);
            return false;
        }
    }

    public static boolean assignTicket(Integer ticketId, Integer assignedTo, String department) {
        String sql = "UPDATE ticket SET assigned_to = ?, assigned_date = NOW(), department = ?, status = 'ĐANG_XỬ_LÝ' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, assignedTo);
            pstmt.setString(2, department);
            pstmt.setInt(3, ticketId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi phân công ticket", e);
            return false;
        }
    }

    public static boolean setSlaDeadline(Integer ticketId, Integer slaHours) {
        String sql = "UPDATE ticket SET sla_hours = ?, sla_deadline = DATE_ADD(NOW(), INTERVAL ? HOUR) WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, slaHours);
            pstmt.setInt(2, slaHours);
            pstmt.setInt(3, ticketId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi set SLA deadline", e);
            return false;
        }
    }

    public static List<Ticket> getTicketsByDepartment(String department) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE department = ? ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket theo bộ phận: {}", department, e);
        }
        return tickets;
    }

    public static List<Ticket> getSlaOverdueTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE sla_deadline < NOW() AND status NOT IN ('HOÀN_THÀNH', 'ĐÓNG_YÊU_CẦU') ORDER BY sla_deadline ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket quá hạn SLA", e);
        }
        return tickets;
    }

    public static List<Ticket> getOverdueTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE status NOT IN ('HOÀN_THÀNH', 'HỦY') ORDER BY created_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ticket ticket = mapResultSetToTicket(rs);
                if (ticket.isOverdue()) {
                    tickets.add(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy ticket quá hạn", e);
        }
        return tickets;
    }

    private static Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setTicketNumber(rs.getString("ticket_number"));
        
        Integer apartmentId = rs.getObject("apartment_id", Integer.class);
        if (apartmentId != null) {
            ticket.setApartmentId(apartmentId);
        }
        
        ticket.setResidentId(rs.getInt("resident_id"));
        ticket.setTicketType(rs.getString("ticket_type"));
        ticket.setCategory(rs.getString("category"));
        ticket.setTitle(rs.getString("title"));
        ticket.setDescription(rs.getString("description"));
        ticket.setAttachmentPath(rs.getString("attachment_path"));
        ticket.setLocation(rs.getString("location"));
        ticket.setPriority(rs.getString("priority"));
        ticket.setStatus(rs.getString("status"));
        ticket.setSubmissionChannel(rs.getString("submission_channel"));
        ticket.setDepartment(rs.getString("department"));
        ticket.setSlaHours(rs.getObject("sla_hours", Integer.class));
        
        Timestamp slaDeadline = rs.getTimestamp("sla_deadline");
        if (slaDeadline != null) {
            ticket.setSlaDeadline(slaDeadline.toLocalDateTime());
        }
        
        Integer receivedBy = rs.getObject("received_by", Integer.class);
        if (receivedBy != null) {
            ticket.setReceivedBy(receivedBy);
        }
        
        Timestamp receivedDate = rs.getTimestamp("received_date");
        if (receivedDate != null) {
            ticket.setReceivedDate(receivedDate.toLocalDateTime());
        }
        
        Timestamp startTime = rs.getTimestamp("start_time");
        if (startTime != null) {
            ticket.setStartTime(startTime.toLocalDateTime());
        }
        
        ticket.setInternalNotes(rs.getString("internal_notes"));
        ticket.setRejectionReason(rs.getString("rejection_reason"));
        ticket.setWantsReopen(rs.getObject("wants_reopen", Boolean.class));
        ticket.setEstimatedCost(rs.getObject("estimated_cost", Double.class));
        ticket.setActualCost(rs.getObject("actual_cost", Double.class));
        ticket.setResolution(rs.getString("resolution"));
        ticket.setSatisfactionRating(rs.getObject("satisfaction_rating", Integer.class));
        ticket.setSatisfactionFeedback(rs.getString("satisfaction_feedback"));

        Integer assignedTo = rs.getObject("assigned_to", Integer.class);
        if (assignedTo != null) {
            ticket.setAssignedTo(assignedTo);
        }

        Timestamp assignedDate = rs.getTimestamp("assigned_date");
        if (assignedDate != null) {
            ticket.setAssignedDate(assignedDate.toLocalDateTime());
        }

        Timestamp createdDate = rs.getTimestamp("created_date");
        if (createdDate != null) {
            ticket.setCreatedDate(createdDate.toLocalDateTime());
        }

        Timestamp resolvedDate = rs.getTimestamp("resolved_date");
        if (resolvedDate != null) {
            ticket.setResolvedDate(resolvedDate.toLocalDateTime());
        }

        Timestamp closedDate = rs.getTimestamp("closed_date");
        if (closedDate != null) {
            ticket.setClosedDate(closedDate.toLocalDateTime());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            ticket.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            ticket.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return ticket;
    }

    // ========== CÁC PHƯƠNG THỨC NÂNG CAO SỬ DỤNG CẤU TRÚC DỮ LIỆU & GIẢI THUẬT TỪ JP1&JP2 ==========

    /**
     * Lấy tickets và sắp xếp theo priority sử dụng Quick Sort
     * Áp dụng thuật toán Quick Sort từ JP1&JP2
     */
    public static List<Ticket> getAllTicketsSortedByPriority() {
        List<Ticket> tickets = getAllTickets();
        SortingUtils.quickSort(tickets, TicketComparators.byPriority());
        return tickets;
    }

    /**
     * Lấy tickets và sắp xếp theo SLA deadline sử dụng Merge Sort
     * Áp dụng thuật toán Merge Sort từ JP1&JP2
     */
    public static List<Ticket> getAllTicketsSortedBySlaDeadline() {
        List<Ticket> tickets = getAllTickets();
        SortingUtils.mergeSort(tickets, TicketComparators.bySlaDeadline());
        return tickets;
    }

    /**
     * Tìm ticket theo ID sử dụng Binary Search (yêu cầu danh sách đã sắp xếp)
     * Áp dụng thuật toán Binary Search từ JP1&JP2
     */
    public static Ticket findTicketByIdBinarySearch(List<Ticket> sortedTickets, Integer ticketId) {
        if (sortedTickets == null || sortedTickets.isEmpty() || ticketId == null) {
            return null;
        }
        
        // Tạo ticket tạm để so sánh
        Ticket searchKey = new Ticket();
        searchKey.setId(ticketId);
        
        int index = SearchUtils.binarySearch(sortedTickets, searchKey, 
            Comparator.comparing(Ticket::getId, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? sortedTickets.get(index) : null;
    }

    /**
     * Tìm ticket theo ticket number sử dụng Linear Search
     * Áp dụng thuật toán Linear Search từ JP1&JP2
     */
    public static Ticket findTicketByNumberLinearSearch(String ticketNumber) {
        List<Ticket> tickets = getAllTickets();
        Ticket searchKey = new Ticket();
        searchKey.setTicketNumber(ticketNumber);
        
        int index = SearchUtils.linearSearch(tickets, searchKey, 
            Comparator.comparing(Ticket::getTicketNumber, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? tickets.get(index) : null;
    }

    /**
     * Nhóm tickets theo status sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<String, List<Ticket>> groupTicketsByStatus() {
        List<Ticket> tickets = getAllTickets();
        return DataStructureUtils.groupBy(tickets, Ticket::getStatus);
    }

    /**
     * Nhóm tickets theo department sử dụng HashMap
     */
    public static Map<String, List<Ticket>> groupTicketsByDepartment() {
        List<Ticket> tickets = getAllTickets();
        return DataStructureUtils.groupBy(tickets, Ticket::getDepartment);
    }

    /**
     * Tạo Map từ tickets với key là ticket ID sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<Integer, Ticket> getTicketsMapById() {
        List<Ticket> tickets = getAllTickets();
        return DataStructureUtils.listToMap(tickets, Ticket::getId);
    }

    /**
     * Tạo TreeMap từ tickets sắp xếp theo priority
     * Áp dụng TreeMap từ JP1&JP2
     */
    public static Map<String, List<Ticket>> getTicketsTreeMapByPriority() {
        List<Ticket> tickets = getAllTickets();
        Map<String, List<Ticket>> grouped = DataStructureUtils.groupBy(tickets, Ticket::getPriority);
        return new TreeMap<>(grouped);
    }

    /**
     * Lấy tickets ưu tiên cao nhất sử dụng PriorityQueue
     * Áp dụng PriorityQueue từ JP1&JP2
     */
    public static List<Ticket> getHighPriorityTickets(int limit) {
        List<Ticket> tickets = getAllTickets();
        PriorityQueue<Ticket> pq = DataStructureUtils.listToPriorityQueue(
            tickets, TicketComparators.byPriority());
        
        List<Ticket> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < limit) {
            result.add(pq.poll());
            count++;
        }
        return result;
    }

    /**
     * Lấy tickets sắp hết hạn SLA sử dụng PriorityQueue
     */
    public static List<Ticket> getUrgentSlaTickets(int limit) {
        List<Ticket> tickets = getAllTickets();
        // Lọc tickets có SLA deadline và chưa hoàn thành
        List<Ticket> urgentTickets = tickets.stream()
            .filter(t -> t.getSlaDeadline() != null && !t.isResolved() && !t.isClosed())
            .collect(Collectors.toList());
        
        PriorityQueue<Ticket> pq = DataStructureUtils.listToPriorityQueue(
            urgentTickets, TicketComparators.bySlaDeadline());
        
        List<Ticket> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < limit) {
            result.add(pq.poll());
            count++;
        }
        return result;
    }

    /**
     * Đếm số lượng tickets theo status sử dụng HashMap
     */
    public static Map<String, Integer> countTicketsByStatus() {
        List<Ticket> tickets = getAllTickets();
        Map<String, Integer> countMap = new HashMap<>();
        
        for (Ticket ticket : tickets) {
            String status = ticket.getStatus() != null ? ticket.getStatus() : "UNKNOWN";
            countMap.put(status, countMap.getOrDefault(status, 0) + 1);
        }
        
        return countMap;
    }

    /**
     * Đếm số lượng tickets theo department sử dụng HashMap
     */
    public static Map<String, Integer> countTicketsByDepartment() {
        List<Ticket> tickets = getAllTickets();
        Map<String, Integer> countMap = new HashMap<>();
        
        for (Ticket ticket : tickets) {
            String department = ticket.getDepartment() != null ? ticket.getDepartment() : "UNKNOWN";
            countMap.put(department, countMap.getOrDefault(department, 0) + 1);
        }
        
        return countMap;
    }

    /**
     * Tìm ticket có priority cao nhất sử dụng thuật toán tìm max
     */
    public static Ticket findHighestPriorityTicket() {
        List<Ticket> tickets = getAllTickets();
        if (tickets.isEmpty()) {
            return null;
        }
        return DataStructureUtils.findMax(tickets, TicketComparators.byPriority());
    }

    /**
     * Sử dụng Stack để lưu lịch sử tickets đã xem
     */
    public static class TicketHistoryStack {
        private DataStructureUtils.Stack<Ticket> historyStack = new DataStructureUtils.Stack<>();
        
        public void pushTicket(Ticket ticket) {
            historyStack.push(ticket);
        }
        
        public Ticket popLastTicket() {
            if (historyStack.isEmpty()) {
                return null;
            }
            return historyStack.pop();
        }
        
        public Ticket peekLastTicket() {
            if (historyStack.isEmpty()) {
                return null;
            }
            return historyStack.peek();
        }
        
        public List<Ticket> getHistory() {
            return historyStack.toList();
        }
        
        public void clearHistory() {
            historyStack.clear();
        }
    }

    /**
     * Sử dụng Queue để quản lý tickets chờ xử lý
     */
    public static class TicketProcessingQueue {
        private DataStructureUtils.Queue<Ticket> processingQueue = new DataStructureUtils.Queue<>();
        
        public void enqueueTicket(Ticket ticket) {
            processingQueue.enqueue(ticket);
        }
        
        public Ticket dequeueNextTicket() {
            if (processingQueue.isEmpty()) {
                return null;
            }
            return processingQueue.dequeue();
        }
        
        public Ticket peekNextTicket() {
            if (processingQueue.isEmpty()) {
                return null;
            }
            return processingQueue.peek();
        }
        
        public List<Ticket> getQueue() {
            return processingQueue.toList();
        }
        
        public void clearQueue() {
            processingQueue.clear();
        }
        
        public int getQueueSize() {
            return processingQueue.size();
        }
    }
}


