package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.UtilityMeter;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý công tơ điện nước
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 5. Điện – Nước – Gas (Utility Billing)
 */
public class UtilityMeterService {

    /**
     * Lấy tất cả công tơ
     */
    public static List<UtilityMeter> getAllMeters() {
        List<UtilityMeter> meters = new ArrayList<>();
        String sql = "SELECT * FROM utility_meter ORDER BY apartment_id, meter_type, period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách công tơ: " + e.getMessage());
            e.printStackTrace();
        }
        return meters;
    }

    /**
     * Lấy công tơ theo căn hộ
     */
    public static List<UtilityMeter> getMetersByApartmentId(Integer apartmentId) {
        List<UtilityMeter> meters = new ArrayList<>();
        String sql = "SELECT * FROM utility_meter WHERE apartment_id = ? " +
                     "ORDER BY meter_type, period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy công tơ theo căn hộ: " + e.getMessage());
            e.printStackTrace();
        }
        return meters;
    }

    /**
     * Lấy chỉ số công tơ mới nhất của một loại
     */
    public static UtilityMeter getLatestMeterReading(Integer apartmentId, String meterType, 
                                                     Integer month, Integer year) {
        String sql = "SELECT * FROM utility_meter WHERE apartment_id = ? AND meter_type = ? " +
                     "AND period_month = ? AND period_year = ? LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            pstmt.setString(2, meterType);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMeter(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chỉ số công tơ: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy chỉ số công tơ kỳ trước
     */
    public static UtilityMeter getPreviousMeterReading(Integer apartmentId, String meterType, 
                                                        Integer month, Integer year) {
        // Tìm kỳ trước
        int prevMonth = month - 1;
        int prevYear = year;
        if (prevMonth <= 0) {
            prevMonth = 12;
            prevYear = year - 1;
        }

        return getLatestMeterReading(apartmentId, meterType, prevMonth, prevYear);
    }

    /**
     * Nhập chỉ số công tơ
     */
    public static boolean addMeterReading(UtilityMeter meter) {
        // Lấy chỉ số kỳ trước
        UtilityMeter previous = getPreviousMeterReading(
            meter.getApartmentId(), meter.getMeterType(), 
            meter.getPeriodMonth(), meter.getPeriodYear());
        
        if (previous != null) {
            meter.setPreviousReading(previous.getCurrentReading());
        }

        // Tính lượng tiêu thụ
        meter.calculateConsumption();
        meter.calculateAmount();

        // Phát hiện bất thường
        if (previous != null && previous.getConsumption() != null && meter.getConsumption() != null) {
            // Tăng > 200% so với kỳ trước
            if (meter.getConsumption() > previous.getConsumption() * 2) {
                meter.setIsAbnormal(true);
            }
        }

        String sql = "INSERT INTO utility_meter (apartment_id, meter_type, meter_code, current_reading, " +
                     "previous_reading, reading_date, period_month, period_year, consumption, unit_price, " +
                     "total_amount, is_abnormal, notes, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, meter.getApartmentId());
            pstmt.setString(2, meter.getMeterType());
            pstmt.setString(3, meter.getMeterCode());
            pstmt.setObject(4, meter.getCurrentReading());
            pstmt.setObject(5, meter.getPreviousReading());
            pstmt.setDate(6, meter.getReadingDate() != null ? 
                Date.valueOf(meter.getReadingDate()) : Date.valueOf(LocalDate.now()));
            pstmt.setInt(7, meter.getPeriodMonth());
            pstmt.setInt(8, meter.getPeriodYear());
            pstmt.setObject(9, meter.getConsumption());
            pstmt.setObject(10, meter.getUnitPrice());
            pstmt.setObject(11, meter.getTotalAmount());
            pstmt.setBoolean(12, meter.getIsAbnormal() != null ? meter.getIsAbnormal() : false);
            pstmt.setString(13, meter.getNotes());
            LocalDate now = LocalDate.now();
            pstmt.setDate(14, Date.valueOf(now));
            pstmt.setDate(15, Date.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    meter.setId(rs.getInt(1));
                }
                System.out.println("Đã nhập chỉ số công tơ: " + meter.getMeterCode() + 
                    " - Chỉ số: " + meter.getCurrentReading());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi nhập chỉ số công tơ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Nhập chỉ số đồng loạt từ danh sách
     */
    public static int batchImportMeterReadings(List<UtilityMeter> meters) {
        int count = 0;
        for (UtilityMeter meter : meters) {
            if (addMeterReading(meter)) {
                count++;
            }
        }
        System.out.println("Đã nhập " + count + "/" + meters.size() + " chỉ số công tơ");
        return count;
    }

    /**
     * Lấy lịch sử công tơ theo căn hộ và loại
     */
    public static List<UtilityMeter> getMeterHistory(Integer apartmentId, String meterType) {
        List<UtilityMeter> meters = new ArrayList<>();
        String sql = "SELECT * FROM utility_meter WHERE apartment_id = ? AND meter_type = ? " +
                     "ORDER BY period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            pstmt.setString(2, meterType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch sử công tơ: " + e.getMessage());
            e.printStackTrace();
        }
        return meters;
    }

    /**
     * Lấy danh sách công tơ có chỉ số bất thường
     */
    public static List<UtilityMeter> getAbnormalMeters(Integer month, Integer year) {
        List<UtilityMeter> meters = new ArrayList<>();
        String sql = "SELECT * FROM utility_meter WHERE is_abnormal = 1 " +
                     "AND period_month = ? AND period_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                meters.add(mapResultSetToMeter(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy công tơ bất thường: " + e.getMessage());
            e.printStackTrace();
        }
        return meters;
    }

    private static UtilityMeter mapResultSetToMeter(ResultSet rs) throws SQLException {
        UtilityMeter meter = new UtilityMeter();
        meter.setId(rs.getInt("id"));
        meter.setApartmentId(rs.getInt("apartment_id"));
        meter.setMeterType(rs.getString("meter_type"));
        meter.setMeterCode(rs.getString("meter_code"));
        
        Double currentReading = rs.getObject("current_reading", Double.class);
        meter.setCurrentReading(currentReading);
        
        Double previousReading = rs.getObject("previous_reading", Double.class);
        meter.setPreviousReading(previousReading);
        
        Date readingDate = rs.getDate("reading_date");
        if (readingDate != null) {
            meter.setReadingDate(readingDate.toLocalDate());
        }
        
        meter.setPeriodMonth(rs.getInt("period_month"));
        meter.setPeriodYear(rs.getInt("period_year"));
        
        Double consumption = rs.getObject("consumption", Double.class);
        meter.setConsumption(consumption);
        
        Double unitPrice = rs.getObject("unit_price", Double.class);
        meter.setUnitPrice(unitPrice);
        
        Double totalAmount = rs.getObject("total_amount", Double.class);
        meter.setTotalAmount(totalAmount);
        
        Boolean isAbnormal = rs.getObject("is_abnormal", Boolean.class);
        meter.setIsAbnormal(isAbnormal != null ? isAbnormal : false);
        
        meter.setNotes(rs.getString("notes"));
        
        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            meter.setCreatedAt(createdAt.toLocalDate());
        }
        
        Date updatedAt = rs.getDate("updated_at");
        if (updatedAt != null) {
            meter.setUpdatedAt(updatedAt.toLocalDate());
        }

        return meter;
    }
}

