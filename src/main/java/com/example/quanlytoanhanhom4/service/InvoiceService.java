package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Invoice;
import com.example.quanlytoanhanhom4.util.DataStructureUtils;
import com.example.quanlytoanhanhom4.util.SearchUtils;
import com.example.quanlytoanhanhom4.util.SortingUtils;
import com.example.quanlytoanhanhom4.util.comparator.InvoiceComparators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice ORDER BY invoice_date DESC, invoice_number";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            logger.debug("Đang thực thi query: {}", sql);
            int count = 0;
            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
                count++;
            }
            logger.info("Đã lấy được {} hóa đơn từ database", count);
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi lấy danh sách hóa đơn", e);
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi lấy danh sách hóa đơn", e);
        }
        return invoices;
    }

    public static List<Invoice> getInvoicesByApartment(int apartmentId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE apartment_id = ? ORDER BY invoice_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoices.add(invoice);
                }
            }
            logger.debug("Đã lấy {} hóa đơn cho căn hộ ID: {}", invoices.size(), apartmentId);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy hóa đơn theo căn hộ ID: {}", apartmentId, e);
        }
        return invoices;
    }

    public static Invoice getInvoiceByNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.trim().isEmpty()) {
            logger.warn("Invoice number không được null hoặc rỗng");
            return null;
        }
        
        String sql = "SELECT * FROM invoice WHERE invoice_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, invoiceNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
            logger.debug("Không tìm thấy hóa đơn với số: {}", invoiceNumber);
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm hóa đơn theo số: {}", invoiceNumber, e);
        }
        return null;
    }

    public static List<Invoice> getInvoicesByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            logger.warn("Status không được null hoặc rỗng");
            return new ArrayList<>();
        }
        
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoice WHERE status = ? ORDER BY invoice_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoices.add(invoice);
                }
            }
            logger.debug("Đã lấy {} hóa đơn với status: {}", invoices.size(), status);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy hóa đơn theo status: {}", status, e);
        }
        return invoices;
    }

    public static Invoice getInvoiceById(int id) {
        if (id <= 0) {
            logger.warn("Invoice ID phải lớn hơn 0, nhận được: {}", id);
            return null;
        }
        
        String sql = "SELECT * FROM invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
            logger.debug("Không tìm thấy hóa đơn với ID: {}", id);
        } catch (SQLException e) {
            logger.error("Lỗi khi tìm hóa đơn theo ID: {}", id, e);
        }
        return null;
    }

    public static boolean addInvoice(Invoice invoice) {
        if (invoice == null) {
            logger.error("Invoice không được null");
            return false;
        }
        
        if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().trim().isEmpty()) {
            logger.error("Invoice number không được null hoặc rỗng");
            return false;
        }
        
        String sql = "INSERT INTO invoice (apartment_id, resident_id, invoice_number, invoice_date, due_date, " +
                "total_amount, paid_amount, remaining_amount, status, payment_method, payment_reference, paid_date, pdf_path, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getApartmentId());
            pstmt.setObject(2, invoice.getResidentId());
            pstmt.setString(3, invoice.getInvoiceNumber());
            pstmt.setDate(4, java.sql.Date.valueOf(invoice.getInvoiceDate()));
            pstmt.setDate(5, java.sql.Date.valueOf(invoice.getDueDate()));
            pstmt.setDouble(6, invoice.getTotalAmount());
            pstmt.setDouble(7, invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0.0);
            pstmt.setDouble(8, invoice.getRemainingAmount() != null ? invoice.getRemainingAmount() : invoice.getTotalAmount());
            pstmt.setString(9, invoice.getStatus() != null ? invoice.getStatus() : "PENDING");
            pstmt.setString(10, invoice.getPaymentMethod());
            pstmt.setString(11, invoice.getPaymentReference());
            pstmt.setObject(12, invoice.getPaidDate() != null ? java.sql.Date.valueOf(invoice.getPaidDate()) : null);
            pstmt.setString(13, invoice.getPdfPath());
            pstmt.setString(14, invoice.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm hóa đơn mới: {}", invoice.getInvoiceNumber());
                return true;
            }
            logger.warn("Không có dòng nào được thêm vào database");
            return false;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm hóa đơn: {}", invoice.getInvoiceNumber(), e);
            return false;
        }
    }

    public static boolean updateInvoice(Invoice invoice) {
        if (invoice == null) {
            logger.error("Invoice không được null");
            return false;
        }
        
        if (invoice.getId() <= 0) {
            logger.error("Invoice ID phải lớn hơn 0, nhận được: {}", invoice.getId());
            return false;
        }
        
        String sql = "UPDATE invoice SET apartment_id = ?, resident_id = ?, invoice_number = ?, invoice_date = ?, " +
                "due_date = ?, total_amount = ?, paid_amount = ?, remaining_amount = ?, " +
                "status = ?, payment_method = ?, payment_reference = ?, paid_date = ?, pdf_path = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invoice.getApartmentId());
            pstmt.setObject(2, invoice.getResidentId());
            pstmt.setString(3, invoice.getInvoiceNumber());
            pstmt.setDate(4, java.sql.Date.valueOf(invoice.getInvoiceDate()));
            pstmt.setDate(5, java.sql.Date.valueOf(invoice.getDueDate()));
            pstmt.setDouble(6, invoice.getTotalAmount());
            pstmt.setDouble(7, invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0.0);
            pstmt.setDouble(8, invoice.getRemainingAmount() != null ? invoice.getRemainingAmount() : invoice.getTotalAmount());
            pstmt.setString(9, invoice.getStatus());
            pstmt.setString(10, invoice.getPaymentMethod());
            pstmt.setString(11, invoice.getPaymentReference());
            pstmt.setObject(12, invoice.getPaidDate() != null ? java.sql.Date.valueOf(invoice.getPaidDate()) : null);
            pstmt.setString(13, invoice.getPdfPath());
            pstmt.setString(14, invoice.getNotes());
            pstmt.setInt(15, invoice.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật hóa đơn ID: {}", invoice.getId());
                return true;
            }
            logger.warn("Không có dòng nào được cập nhật cho hóa đơn ID: {}", invoice.getId());
            return false;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật hóa đơn ID: {}", invoice.getId(), e);
            return false;
        }
    }

    public static boolean deleteInvoice(int id) {
        if (id <= 0) {
            logger.warn("Invoice ID phải lớn hơn 0, nhận được: {}", id);
            return false;
        }
        
        String sql = "DELETE FROM invoice WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã xóa hóa đơn ID: {}", id);
                return true;
            }
            logger.warn("Không tìm thấy hóa đơn ID: {} để xóa", id);
            return false;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa hóa đơn ID: {}", id, e);
            return false;
        }
    }

    private static Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setApartmentId(rs.getInt("apartment_id"));
        
        Integer residentId = rs.getObject("resident_id", Integer.class);
        if (residentId != null) {
            invoice.setResidentId(residentId);
        }
        
        invoice.setInvoiceNumber(rs.getString("invoice_number"));

        java.sql.Date invoiceDate = rs.getDate("invoice_date");
        if (invoiceDate != null) {
            invoice.setInvoiceDate(invoiceDate.toLocalDate());
        }

        java.sql.Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            invoice.setDueDate(dueDate.toLocalDate());
        }

        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setPaidAmount(rs.getDouble("paid_amount"));
        invoice.setRemainingAmount(rs.getDouble("remaining_amount"));
        invoice.setStatus(rs.getString("status"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setPaymentReference(rs.getString("payment_reference"));
        invoice.setPdfPath(rs.getString("pdf_path"));

        java.sql.Date paidDate = rs.getDate("paid_date");
        if (paidDate != null) {
            invoice.setPaidDate(paidDate.toLocalDate());
        }

        invoice.setNotes(rs.getString("notes"));

        // Đọc created_at và updated_at
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            invoice.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            invoice.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return invoice;
    }

    // ========== CÁC PHƯƠNG THỨC NÂNG CAO SỬ DỤNG CẤU TRÚC DỮ LIỆU & GIẢI THUẬT TỪ JP1&JP2 ==========

    /**
     * Lấy invoices và sắp xếp theo số tiền sử dụng Quick Sort
     * Áp dụng thuật toán Quick Sort từ JP1&JP2
     */
    public static List<Invoice> getAllInvoicesSortedByAmount() {
        List<Invoice> invoices = getAllInvoices();
        SortingUtils.quickSort(invoices, InvoiceComparators.byAmountDescending());
        return invoices;
    }

    /**
     * Lấy invoices và sắp xếp theo ngày đến hạn sử dụng Merge Sort
     * Áp dụng thuật toán Merge Sort từ JP1&JP2
     */
    public static List<Invoice> getAllInvoicesSortedByDueDate() {
        List<Invoice> invoices = getAllInvoices();
        SortingUtils.mergeSort(invoices, InvoiceComparators.byDueDate());
        return invoices;
    }

    /**
     * Tìm invoice theo ID sử dụng Binary Search (yêu cầu danh sách đã sắp xếp)
     * Áp dụng thuật toán Binary Search từ JP1&JP2
     */
    public static Invoice findInvoiceByIdBinarySearch(List<Invoice> sortedInvoices, Integer invoiceId) {
        if (sortedInvoices == null || sortedInvoices.isEmpty() || invoiceId == null) {
            return null;
        }
        
        Invoice searchKey = new Invoice();
        searchKey.setId(invoiceId);
        
        int index = SearchUtils.binarySearch(sortedInvoices, searchKey, 
            Comparator.comparing(Invoice::getId, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? sortedInvoices.get(index) : null;
    }

    /**
     * Tìm invoice theo invoice number sử dụng Linear Search
     * Áp dụng thuật toán Linear Search từ JP1&JP2
     */
    public static Invoice findInvoiceByNumberLinearSearch(String invoiceNumber) {
        List<Invoice> invoices = getAllInvoices();
        Invoice searchKey = new Invoice();
        searchKey.setInvoiceNumber(invoiceNumber);
        
        int index = SearchUtils.linearSearch(invoices, searchKey, 
            Comparator.comparing(Invoice::getInvoiceNumber, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? invoices.get(index) : null;
    }

    /**
     * Nhóm invoices theo status sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<String, List<Invoice>> groupInvoicesByStatus() {
        List<Invoice> invoices = getAllInvoices();
        return DataStructureUtils.groupBy(invoices, Invoice::getStatus);
    }

    /**
     * Nhóm invoices theo apartment ID sử dụng HashMap
     */
    public static Map<Integer, List<Invoice>> groupInvoicesByApartment() {
        List<Invoice> invoices = getAllInvoices();
        return DataStructureUtils.groupBy(invoices, Invoice::getApartmentId);
    }

    /**
     * Tạo Map từ invoices với key là invoice ID sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<Integer, Invoice> getInvoicesMapById() {
        List<Invoice> invoices = getAllInvoices();
        return DataStructureUtils.listToMap(invoices, Invoice::getId);
    }

    /**
     * Tạo TreeMap từ invoices sắp xếp theo status
     * Áp dụng TreeMap từ JP1&JP2
     */
    public static Map<String, List<Invoice>> getInvoicesTreeMapByStatus() {
        List<Invoice> invoices = getAllInvoices();
        Map<String, List<Invoice>> grouped = DataStructureUtils.groupBy(invoices, Invoice::getStatus);
        return new TreeMap<>(grouped);
    }

    /**
     * Lấy invoices có số tiền cao nhất sử dụng PriorityQueue
     * Áp dụng PriorityQueue từ JP1&JP2
     */
    public static List<Invoice> getTopAmountInvoices(int limit) {
        List<Invoice> invoices = getAllInvoices();
        PriorityQueue<Invoice> pq = DataStructureUtils.listToPriorityQueue(
            invoices, InvoiceComparators.byAmountDescending());
        
        List<Invoice> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < limit) {
            result.add(pq.poll());
            count++;
        }
        return result;
    }

    /**
     * Lấy invoices sắp đến hạn sử dụng PriorityQueue
     */
    public static List<Invoice> getUpcomingDueInvoices(int limit) {
        List<Invoice> invoices = getAllInvoices();
        // Lọc invoices chưa thanh toán và có due date
        List<Invoice> upcomingInvoices = invoices.stream()
            .filter(i -> !"ĐÃ_THANH_TOÁN".equals(i.getStatus()) && i.getDueDate() != null)
            .collect(Collectors.toList());
        
        PriorityQueue<Invoice> pq = DataStructureUtils.listToPriorityQueue(
            upcomingInvoices, InvoiceComparators.byDueDate());
        
        List<Invoice> result = new ArrayList<>();
        int count = 0;
        while (!pq.isEmpty() && count < limit) {
            result.add(pq.poll());
            count++;
        }
        return result;
    }

    /**
     * Đếm số lượng invoices theo status sử dụng HashMap
     */
    public static Map<String, Integer> countInvoicesByStatus() {
        List<Invoice> invoices = getAllInvoices();
        Map<String, Integer> countMap = new HashMap<>();
        
        for (Invoice invoice : invoices) {
            String status = invoice.getStatus() != null ? invoice.getStatus() : "UNKNOWN";
            countMap.put(status, countMap.getOrDefault(status, 0) + 1);
        }
        
        return countMap;
    }

    /**
     * Tính tổng số tiền theo status sử dụng HashMap
     */
    public static Map<String, Double> sumAmountByStatus() {
        List<Invoice> invoices = getAllInvoices();
        Map<String, Double> sumMap = new HashMap<>();
        
        for (Invoice invoice : invoices) {
            String status = invoice.getStatus() != null ? invoice.getStatus() : "UNKNOWN";
            Double amount = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0.0;
            sumMap.put(status, sumMap.getOrDefault(status, 0.0) + amount);
        }
        
        return sumMap;
    }

    /**
     * Tìm invoice có số tiền cao nhất sử dụng thuật toán tìm max
     */
    public static Invoice findHighestAmountInvoice() {
        List<Invoice> invoices = getAllInvoices();
        if (invoices.isEmpty()) {
            return null;
        }
        return DataStructureUtils.findMax(invoices, InvoiceComparators.byAmountDescending());
    }

    /**
     * Tìm invoice có số tiền thấp nhất sử dụng thuật toán tìm min
     */
    public static Invoice findLowestAmountInvoice() {
        List<Invoice> invoices = getAllInvoices();
        if (invoices.isEmpty()) {
            return null;
        }
        return DataStructureUtils.findMin(invoices, InvoiceComparators.byAmountAscending());
    }

    /**
     * Lấy invoices quá hạn sử dụng HashSet để loại bỏ trùng lặp
     */
    public static Set<Invoice> getOverdueInvoicesSet() {
        List<Invoice> invoices = getAllInvoices();
        Set<Invoice> overdueSet = new HashSet<>();
        
        for (Invoice invoice : invoices) {
            if (invoice.isOverdue()) {
                overdueSet.add(invoice);
            }
        }
        
        return overdueSet;
    }

    /**
     * Lấy invoices sắp xếp theo TreeSet (theo invoice number)
     */
    public static Set<Invoice> getInvoicesTreeSet() {
        List<Invoice> invoices = getAllInvoices();
        return DataStructureUtils.listToTreeSet(invoices, InvoiceComparators.byInvoiceNumber());
    }
}


