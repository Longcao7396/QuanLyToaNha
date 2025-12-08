package com.example.quanlytoanhanhom4.util.comparator;

import com.example.quanlytoanhanhom4.model.Invoice;

import java.util.Comparator;

/**
 * Các Comparator cho Invoice model
 * Áp dụng kiến thức từ JP1&JP2 về Comparator interface
 */
public class InvoiceComparators {

    /**
     * Sắp xếp theo số tiền (giảm dần)
     */
    public static Comparator<Invoice> byAmountDescending() {
        return (i1, i2) -> {
            Double amount1 = i1.getTotalAmount() != null ? i1.getTotalAmount() : 0.0;
            Double amount2 = i2.getTotalAmount() != null ? i2.getTotalAmount() : 0.0;
            return Double.compare(amount2, amount1);
        };
    }

    /**
     * Sắp xếp theo số tiền (tăng dần)
     */
    public static Comparator<Invoice> byAmountAscending() {
        return (i1, i2) -> {
            Double amount1 = i1.getTotalAmount() != null ? i1.getTotalAmount() : 0.0;
            Double amount2 = i2.getTotalAmount() != null ? i2.getTotalAmount() : 0.0;
            return Double.compare(amount1, amount2);
        };
    }

    /**
     * Sắp xếp theo ngày hóa đơn (mới nhất trước)
     */
    public static Comparator<Invoice> byInvoiceDateDescending() {
        return (i1, i2) -> {
            if (i1.getInvoiceDate() == null && i2.getInvoiceDate() == null) return 0;
            if (i1.getInvoiceDate() == null) return 1;
            if (i2.getInvoiceDate() == null) return -1;
            return i2.getInvoiceDate().compareTo(i1.getInvoiceDate());
        };
    }

    /**
     * Sắp xếp theo ngày hóa đơn (cũ nhất trước)
     */
    public static Comparator<Invoice> byInvoiceDateAscending() {
        return (i1, i2) -> {
            if (i1.getInvoiceDate() == null && i2.getInvoiceDate() == null) return 0;
            if (i1.getInvoiceDate() == null) return 1;
            if (i2.getInvoiceDate() == null) return -1;
            return i1.getInvoiceDate().compareTo(i2.getInvoiceDate());
        };
    }

    /**
     * Sắp xếp theo ngày đến hạn (sắp hết hạn trước)
     */
    public static Comparator<Invoice> byDueDate() {
        return (i1, i2) -> {
            if (i1.getDueDate() == null && i2.getDueDate() == null) return 0;
            if (i1.getDueDate() == null) return 1;
            if (i2.getDueDate() == null) return -1;
            return i1.getDueDate().compareTo(i2.getDueDate());
        };
    }

    /**
     * Sắp xếp theo status
     */
    public static Comparator<Invoice> byStatus() {
        return Comparator.comparing(i -> i.getStatus() != null ? i.getStatus() : "");
    }

    /**
     * Sắp xếp theo số tiền còn lại (giảm dần)
     */
    public static Comparator<Invoice> byRemainingAmountDescending() {
        return (i1, i2) -> {
            Double remaining1 = i1.getRemainingAmount() != null ? i1.getRemainingAmount() : 0.0;
            Double remaining2 = i2.getRemainingAmount() != null ? i2.getRemainingAmount() : 0.0;
            return Double.compare(remaining2, remaining1);
        };
    }

    /**
     * Sắp xếp theo invoice number
     */
    public static Comparator<Invoice> byInvoiceNumber() {
        return Comparator.comparing(i -> i.getInvoiceNumber() != null ? i.getInvoiceNumber() : "");
    }

    /**
     * Sắp xếp kết hợp: Status -> Due Date -> Amount
     */
    public static Comparator<Invoice> byStatusThenDueDateThenAmount() {
        return byStatus()
                .thenComparing(byDueDate())
                .thenComparing(byAmountDescending());
    }
}








