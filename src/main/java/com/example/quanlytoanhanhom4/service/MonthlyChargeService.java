package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho sinh phí tháng tự động
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 2. Tạo phí tháng tự động (Monthly Charge Generation)
 */
public class MonthlyChargeService {
    private static final Logger logger = LoggerFactory.getLogger(MonthlyChargeService.class);

    /**
     * Sinh phí tự động cho tất cả căn hộ trong tháng/năm
     */
    public static int generateMonthlyCharges(int month, int year, String buildingBlock) {
        int count = 0;
        List<Apartment> apartments;
        
        // Lấy danh sách căn hộ
        if (buildingBlock != null && !buildingBlock.isEmpty()) {
            apartments = ApartmentService.getAllApartments().stream()
                .filter(apt -> buildingBlock.equals(apt.getBuildingBlock()))
                .collect(java.util.stream.Collectors.toList());
        } else {
            apartments = ApartmentService.getAllApartments();
        }

        // Lấy danh sách loại phí tự động
        List<ServiceFeeType> autoFeeTypes = ServiceFeeTypeService.getAllFeeTypes().stream()
            .filter(fee -> fee.getAutoGenerate() != null && fee.getAutoGenerate() && 
                          fee.getIsActive() != null && fee.getIsActive())
            .collect(java.util.stream.Collectors.toList());

        for (Apartment apartment : apartments) {
            // Chỉ sinh phí cho căn hộ đang có người ở hoặc cho thuê
            if (apartment.getStatus() != null && 
                (apartment.getStatus().equals("ĐANG_Ở") || apartment.getStatus().equals("CHO_THUÊ"))) {
                
                if (generateChargesForApartment(apartment, month, year, autoFeeTypes)) {
                    count++;
                }
            }
        }

        logger.info("Đã sinh phí cho {} căn hộ trong tháng {}/{}", count, month, year);
        return count;
    }

    /**
     * Sinh phí cho một căn hộ cụ thể
     */
    private static boolean generateChargesForApartment(Apartment apartment, int month, int year, 
                                                       List<ServiceFeeType> feeTypes) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Kiểm tra xem đã có hóa đơn cho tháng này chưa
                if (invoiceExists(conn, apartment.getId(), month, year)) {
                    logger.warn("Căn hộ {} đã có hóa đơn cho tháng {}/{}", 
                        apartment.getApartmentCode(), month, year);
                    conn.rollback();
                    return false;
                }

                // Tạo hóa đơn
                Invoice invoice = createInvoice(conn, apartment, month, year);
                if (invoice == null) {
                    conn.rollback();
                    return false;
                }

                double totalAmount = 0.0;
                List<InvoiceItem> items = new ArrayList<>();

                // Tính từng loại phí
                for (ServiceFeeType feeType : feeTypes) {
                    InvoiceItem item = calculateFee(apartment, feeType, month, year);
                    if (item != null) {
                        item.setInvoiceId(invoice.getId());
                        if (addInvoiceItem(conn, item)) {
                            items.add(item);
                            totalAmount += item.getAmount() != null ? item.getAmount() : 0;
                        }
                    }
                }

                // Cập nhật tổng tiền hóa đơn
                updateInvoiceTotal(conn, invoice.getId(), totalAmount);

                conn.commit();
                logger.info("Đã sinh phí cho căn hộ {} - Tổng: {}", apartment.getApartmentCode(), totalAmount);
                return true;
            } catch (Exception e) {
                conn.rollback();
                logger.error("Lỗi khi sinh phí cho căn hộ {}", apartment.getApartmentCode(), e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error("Lỗi kết nối database", e);
            return false;
        }
    }

    /**
     * Tính phí cho một loại phí cụ thể
     */
    private static InvoiceItem calculateFee(Apartment apartment, ServiceFeeType feeType, int month, int year) {
        InvoiceItem item = new InvoiceItem();
        item.setFeeTypeId(feeType.getId());
        item.setFeeName(feeType.getFeeName());
        item.setUnit(feeType.getUnit());
        item.setUnitPrice(feeType.getUnitPrice());

        String method = feeType.getCalculationMethod();
        
        if ("CỐ_ĐỊNH".equals(method)) {
            // Phí cố định
            item.setQuantity(1.0);
            item.calculateAmount();
        } else if ("THEO_DIỆN_TÍCH".equals(method)) {
            // Phí theo diện tích
            if (apartment.getArea() != null && feeType.getUnitPrice() != null) {
                item.setQuantity(apartment.getArea());
                item.calculateAmount();
            } else {
                return null;
            }
        } else if ("THEO_SỐ_LƯỢNG".equals(method)) {
            // Phí theo số lượng (xe, người, etc.)
            // Cần lấy từ thông tin cư dân hoặc căn hộ
            // Tạm thời dùng số người tối đa hoặc 1
            Double quantity = apartment.getMaxOccupants() != null ? 
                apartment.getMaxOccupants().doubleValue() : 1.0;
            item.setQuantity(quantity);
            item.calculateAmount();
        } else if ("THEO_CÔNG_TƠ".equals(method)) {
            // Phí theo công tơ (điện, nước)
            UtilityMeter meter = UtilityMeterService.getLatestMeterReading(
                apartment.getId(), feeType.getFeeCategory(), month, year);
            if (meter != null && meter.getConsumption() != null) {
                item.setQuantity(meter.getConsumption());
                item.setUnitPrice(meter.getUnitPrice() != null ? meter.getUnitPrice() : feeType.getUnitPrice());
                item.calculateAmount();
            } else {
                // Chưa có chỉ số công tơ, có thể bỏ qua hoặc đánh dấu
                return null;
            }
        }

        return item;
    }

    /**
     * Kiểm tra hóa đơn đã tồn tại chưa
     */
    private static boolean invoiceExists(Connection conn, Integer apartmentId, int month, int year) 
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM invoice WHERE apartment_id = ? AND period_month = ? AND period_year = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Tạo hóa đơn mới
     */
    private static Invoice createInvoice(Connection conn, Apartment apartment, int month, int year) 
            throws SQLException {
        String invoiceNumber = generateInvoiceNumber(apartment, month, year);
        LocalDate invoiceDate = LocalDate.of(year, month, 1);
        LocalDate dueDate = invoiceDate.plusDays(15); // Hạn thanh toán: 15 ngày

        String sql = "INSERT INTO invoice (invoice_number, apartment_id, resident_id, period_month, period_year, " +
                     "invoice_date, due_date, total_amount, paid_amount, remaining_amount, status, is_locked, " +
                     "is_sent, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 'CHƯA_THANH_TOÁN', 0, 0, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, invoiceNumber);
            pstmt.setInt(2, apartment.getId());
            
            // Lấy chủ hộ hoặc cư dân
            Resident resident = ResidentService.getResidentByApartmentId(apartment.getId());
            pstmt.setObject(3, resident != null ? resident.getId() : null);
            
            pstmt.setInt(4, month);
            pstmt.setInt(5, year);
            pstmt.setDate(6, Date.valueOf(invoiceDate));
            pstmt.setDate(7, Date.valueOf(dueDate));
            pstmt.setTimestamp(8, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setTimestamp(9, Timestamp.valueOf(java.time.LocalDateTime.now()));

            if (pstmt.executeUpdate() > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt(1));
                    invoice.setInvoiceNumber(invoiceNumber);
                    invoice.setApartmentId(apartment.getId());
                    invoice.setPeriodMonth(month);
                    invoice.setPeriodYear(year);
                    invoice.setInvoiceDate(invoiceDate);
                    invoice.setDueDate(dueDate);
                    return invoice;
                }
            }
        }
        return null;
    }

    /**
     * Thêm chi tiết hóa đơn
     */
    private static boolean addInvoiceItem(Connection conn, InvoiceItem item) throws SQLException {
        String sql = "INSERT INTO invoice_item (invoice_id, fee_type_id, fee_name, description, quantity, " +
                     "unit, unit_price, amount, apartment_service_fee_id, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getInvoiceId());
            pstmt.setInt(2, item.getFeeTypeId());
            pstmt.setString(3, item.getFeeName());
            pstmt.setString(4, item.getDescription());
            pstmt.setObject(5, item.getQuantity());
            pstmt.setString(6, item.getUnit());
            pstmt.setObject(7, item.getUnitPrice());
            pstmt.setObject(8, item.getAmount());
            pstmt.setObject(9, item.getApartmentServiceFeeId());
            pstmt.setString(10, item.getNotes());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật tổng tiền hóa đơn
     */
    private static void updateInvoiceTotal(Connection conn, Integer invoiceId, Double totalAmount) 
            throws SQLException {
        String sql = "UPDATE invoice SET total_amount = ?, remaining_amount = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, totalAmount);
            pstmt.setDouble(2, totalAmount);
            pstmt.setInt(3, invoiceId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Tạo số hóa đơn
     */
    private static String generateInvoiceNumber(Apartment apartment, int month, int year) {
        String block = apartment.getBuildingBlock() != null ? apartment.getBuildingBlock() : "A";
        String aptCode = apartment.getApartmentCode() != null ? 
            apartment.getApartmentCode().replace("-", "") : apartment.getApartmentNo();
        return String.format("HD-%s-%s-%02d%04d", block, aptCode, month, year);
    }

    /**
     * Xem trước phí (preview) cho một căn hộ
     */
    public static List<InvoiceItem> previewCharges(Apartment apartment, int month, int year) {
        List<InvoiceItem> items = new ArrayList<>();
        List<ServiceFeeType> feeTypes = ServiceFeeTypeService.getAllFeeTypes().stream()
            .filter(fee -> fee.getIsActive() != null && fee.getIsActive())
            .collect(java.util.stream.Collectors.toList());

        for (ServiceFeeType feeType : feeTypes) {
            InvoiceItem item = calculateFee(apartment, feeType, month, year);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }
}

