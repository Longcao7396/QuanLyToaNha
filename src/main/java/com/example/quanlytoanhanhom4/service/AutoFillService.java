package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.model.ApartmentServiceFee;
import com.example.quanlytoanhanhom4.model.Invoice;
import com.example.quanlytoanhanhom4.model.Resident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Service tự động điền dữ liệu vào form
 * Tối ưu hóa: Giảm thiểu việc nhập thủ công, tự động điền thông tin
 */
public class AutoFillService {
    
    private static final Logger logger = LoggerFactory.getLogger(AutoFillService.class);
    
    /**
     * Tự động điền thông tin căn hộ vào form
     */
    public static Apartment autoFillApartmentInfo(Integer apartmentId) {
        if (apartmentId == null) {
            return null;
        }
        
        try {
            Apartment apartment = ApartmentService.getApartmentById(apartmentId);
            return apartment;
        } catch (Exception e) {
            logger.error("Lỗi khi tự động điền thông tin căn hộ", e);
            return null;
        }
    }
    
    /**
     * Tự động điền thông tin phí dịch vụ cuối cùng cho căn hộ
     */
    public static ApartmentServiceFee autoFillLastServiceFee(Integer apartmentId, Integer feeTypeId) {
        if (apartmentId == null || feeTypeId == null) {
            return null;
        }
        
        try {
            List<ApartmentServiceFee> fees = ApartmentServiceFeeService.getServiceFeesByApartmentId(apartmentId);
            
            // Tìm phí dịch vụ cùng loại gần nhất
            ApartmentServiceFee lastFee = fees.stream()
                .filter(fee -> fee.getFeeTypeId().equals(feeTypeId))
                .sorted((f1, f2) -> {
                    // Sắp xếp theo period (tháng/năm)
                    if (f1.getPeriodMonth() != null && f2.getPeriodMonth() != null) {
                        int monthCompare = f1.getPeriodMonth().compareTo(f2.getPeriodMonth());
                        if (monthCompare != 0) return monthCompare;
                    }
                    if (f1.getPeriodYear() != null && f2.getPeriodYear() != null) {
                        return f2.getPeriodYear().compareTo(f1.getPeriodYear());
                    }
                    return 0;
                })
                .findFirst()
                .orElse(null);
            
            return lastFee;
        } catch (Exception e) {
            logger.error("Lỗi khi tự động điền phí dịch vụ cuối", e);
            return null;
        }
    }
    
    /**
     * Tự động điền chỉ số điện/nước cuối cùng
     */
    public static Double autoFillLastReading(Integer apartmentId, Integer feeTypeId) {
        ApartmentServiceFee lastFee = autoFillLastServiceFee(apartmentId, feeTypeId);
        if (lastFee != null && lastFee.getCurrentReading() != null) {
            return lastFee.getCurrentReading();
        }
        return 0.0;
    }
    
    /**
     * Tự động tính tổng tiền từ các phí dịch vụ chưa có hóa đơn
     */
    public static Double autoCalculateTotalFromUnbilledFees(Integer apartmentId, Integer month, Integer year) {
        if (apartmentId == null) {
            return 0.0;
        }
        
        try {
            List<ApartmentServiceFee> fees = ApartmentServiceFeeService.getServiceFeesByApartmentAndPeriod(
                apartmentId, month != null ? month : LocalDate.now().getMonthValue(),
                year != null ? year : LocalDate.now().getYear());
            
            return fees.stream()
                .filter(fee -> fee.getStatus() == null || !fee.getStatus().equals("INVOICED"))
                .mapToDouble(fee -> fee.getTotalAmount() != null ? fee.getTotalAmount() : 0.0)
                .sum();
        } catch (Exception e) {
            logger.error("Lỗi khi tự động tính tổng từ phí dịch vụ", e);
            return 0.0;
        }
    }
    
    /**
     * Tự động tạo số hóa đơn tiếp theo
     */
    public static String autoGenerateInvoiceNumber(Integer apartmentId) {
        if (apartmentId == null) {
            return null;
        }
        
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        
        // Kiểm tra xem đã có hóa đơn cho tháng này chưa
        List<Invoice> existingInvoices = InvoiceService.getInvoicesByApartment(apartmentId);
        long count = existingInvoices.stream()
            .filter(inv -> inv.getInvoiceDate() != null &&
                          inv.getInvoiceDate().getMonthValue() == month &&
                          inv.getInvoiceDate().getYear() == year)
            .count();
        
        String monthStr = String.format("%02d", month);
        return String.format("HD-%03d-%s%d-%02d", apartmentId, monthStr, year, count + 1);
    }
    
    /**
     * Tự động tính ngày đến hạn thanh toán (15 ngày sau ngày tạo)
     */
    public static LocalDate autoCalculateDueDate(LocalDate invoiceDate) {
        if (invoiceDate == null) {
            invoiceDate = LocalDate.now();
        }
        // Mặc định 15 ngày sau
        return invoiceDate.plusDays(15);
    }
    
    /**
     * Tự động cập nhật trạng thái hóa đơn dựa trên số tiền đã thanh toán
     */
    public static String autoUpdateInvoiceStatus(Double totalAmount, Double paidAmount) {
        if (totalAmount == null || totalAmount <= 0) {
            return "CHỜ_THANH_TOÁN";
        }
        
        if (paidAmount == null) {
            paidAmount = 0.0;
        }
        
        if (paidAmount >= totalAmount) {
            return "ĐÃ_THANH_TOÁN";
        } else if (paidAmount > 0) {
            return "THANH_TOÁN_MỘT_PHẦN";
        } else {
            return "CHỜ_THANH_TOÁN";
        }
    }
    
    /**
     * Tự động tính công nợ của căn hộ
     */
    public static Double autoCalculateDebt(Integer apartmentId) {
        if (apartmentId == null) {
            return 0.0;
        }
        
        try {
            List<Invoice> invoices = InvoiceService.getInvoicesByApartment(apartmentId);
            
            return invoices.stream()
                .filter(inv -> {
                    String status = inv.getStatus();
                    return status != null && 
                           (status.equals("CHỜ_THANH_TOÁN") || 
                            status.equals("THANH_TOÁN_MỘT_PHẦN") ||
                            status.equals("QUÁ_HẠN"));
                })
                .mapToDouble(inv -> {
                    Double remaining = inv.getRemainingAmount();
                    return remaining != null ? remaining : 0.0;
                })
                .sum();
        } catch (Exception e) {
            logger.error("Lỗi khi tự động tính công nợ", e);
            return 0.0;
        }
    }
    
    /**
     * Tự động tìm cư dân theo CMND/CCCD để điền thông tin
     */
    public static Resident autoFillResidentByIdentityCard(String identityCard) {
        if (identityCard == null || identityCard.trim().isEmpty()) {
            return null;
        }
        
        try {
            List<Resident> allResidents = ResidentService.getAllResidents();
            return allResidents.stream()
                .filter(r -> identityCard.trim().equals(r.getIdentityCard()))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            logger.error("Lỗi khi tự động tìm cư dân theo CMND/CCCD", e);
            return null;
        }
    }
    
    /**
     * Tự động tìm cư dân theo số điện thoại để điền thông tin
     */
    public static Resident autoFillResidentByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        
        try {
            List<Resident> allResidents = ResidentService.getAllResidents();
            String normalizedPhone = phone.trim().replaceAll("[^0-9]", "");
            return allResidents.stream()
                .filter(r -> {
                    String residentPhone = r.getPhone();
                    if (residentPhone == null) return false;
                    String normalizedResidentPhone = residentPhone.replaceAll("[^0-9]", "");
                    return normalizedPhone.equals(normalizedResidentPhone);
                })
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            logger.error("Lỗi khi tự động tìm cư dân theo số điện thoại", e);
            return null;
        }
    }
    
    /**
     * Tự động điền địa chỉ từ căn hộ
     */
    public static String autoFillAddressFromApartment(Integer apartmentId) {
        if (apartmentId == null) {
            return null;
        }
        
        try {
            Apartment apartment = ApartmentService.getApartmentById(apartmentId);
            if (apartment != null) {
                StringBuilder address = new StringBuilder();
                if (apartment.getBuildingBlock() != null && !apartment.getBuildingBlock().isEmpty()) {
                    address.append("Tòa ").append(apartment.getBuildingBlock());
                }
                if (apartment.getFloorNumber() != null) {
                    if (address.length() > 0) address.append(", ");
                    address.append("Tầng ").append(apartment.getFloorNumber());
                }
                if (apartment.getApartmentNo() != null && !apartment.getApartmentNo().isEmpty()) {
                    if (address.length() > 0) address.append(", ");
                    address.append("Căn hộ ").append(apartment.getApartmentNo());
                }
                return address.length() > 0 ? address.toString() : null;
            }
        } catch (Exception e) {
            logger.error("Lỗi khi tự động điền địa chỉ từ căn hộ", e);
        }
        return null;
    }
    
    /**
     * Tự động format số điện thoại (loại bỏ ký tự không phải số)
     */
    public static String autoFormatPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }
        // Chỉ giữ lại số
        return phone.replaceAll("[^0-9]", "");
    }
    
    /**
     * Tự động format CMND/CCCD (loại bỏ ký tự không phải số)
     */
    public static String autoFormatIdentityCard(String identityCard) {
        if (identityCard == null || identityCard.trim().isEmpty()) {
            return "";
        }
        // Chỉ giữ lại số
        return identityCard.replaceAll("[^0-9]", "");
    }
}

