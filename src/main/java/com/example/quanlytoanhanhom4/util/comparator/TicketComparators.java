package com.example.quanlytoanhanhom4.util.comparator;

import com.example.quanlytoanhanhom4.model.Ticket;

import java.util.Comparator;

/**
 * Các Comparator cho Ticket model
 * Áp dụng kiến thức từ JP1&JP2 về Comparator interface
 */
public class TicketComparators {

    /**
     * Sắp xếp theo priority (ưu tiên): KHẨN_CẤP > CAO > TRUNG_BÌNH > THẤP
     */
    public static Comparator<Ticket> byPriority() {
        return (t1, t2) -> {
            int priority1 = getPriorityValue(t1.getPriority());
            int priority2 = getPriorityValue(t2.getPriority());
            return Integer.compare(priority2, priority1); // Descending order
        };
    }

    private static int getPriorityValue(String priority) {
        if (priority == null) return 0;
        switch (priority) {
            case "KHẨN_CẤP": return 4;
            case "CAO": return 3;
            case "TRUNG_BÌNH": return 2;
            case "THẤP": return 1;
            default: return 0;
        }
    }

    /**
     * Sắp xếp theo ngày tạo (mới nhất trước)
     */
    public static Comparator<Ticket> byCreatedDateDescending() {
        return (t1, t2) -> {
            if (t1.getCreatedDate() == null && t2.getCreatedDate() == null) return 0;
            if (t1.getCreatedDate() == null) return 1;
            if (t2.getCreatedDate() == null) return -1;
            return t2.getCreatedDate().compareTo(t1.getCreatedDate());
        };
    }

    /**
     * Sắp xếp theo ngày tạo (cũ nhất trước)
     */
    public static Comparator<Ticket> byCreatedDateAscending() {
        return (t1, t2) -> {
            if (t1.getCreatedDate() == null && t2.getCreatedDate() == null) return 0;
            if (t1.getCreatedDate() == null) return 1;
            if (t2.getCreatedDate() == null) return -1;
            return t1.getCreatedDate().compareTo(t2.getCreatedDate());
        };
    }

    /**
     * Sắp xếp theo SLA deadline (sắp hết hạn trước)
     */
    public static Comparator<Ticket> bySlaDeadline() {
        return (t1, t2) -> {
            if (t1.getSlaDeadline() == null && t2.getSlaDeadline() == null) return 0;
            if (t1.getSlaDeadline() == null) return 1;
            if (t2.getSlaDeadline() == null) return -1;
            return t1.getSlaDeadline().compareTo(t2.getSlaDeadline());
        };
    }

    /**
     * Sắp xếp theo status
     */
    public static Comparator<Ticket> byStatus() {
        return Comparator.comparing(t -> t.getStatus() != null ? t.getStatus() : "");
    }

    /**
     * Sắp xếp theo department
     */
    public static Comparator<Ticket> byDepartment() {
        return Comparator.comparing(t -> t.getDepartment() != null ? t.getDepartment() : "");
    }

    /**
     * Sắp xếp kết hợp: Priority -> SLA Deadline -> Created Date
     */
    public static Comparator<Ticket> byPriorityThenSlaThenDate() {
        return byPriority()
                .thenComparing(bySlaDeadline())
                .thenComparing(byCreatedDateDescending());
    }

    /**
     * Sắp xếp theo ticket number
     */
    public static Comparator<Ticket> byTicketNumber() {
        return Comparator.comparing(t -> t.getTicketNumber() != null ? t.getTicketNumber() : "");
    }
}








