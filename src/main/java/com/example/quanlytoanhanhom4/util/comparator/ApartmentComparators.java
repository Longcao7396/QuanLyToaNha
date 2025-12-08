package com.example.quanlytoanhanhom4.util.comparator;

import com.example.quanlytoanhanhom4.model.Apartment;

import java.util.Comparator;

/**
 * Các Comparator cho Apartment model
 * Áp dụng kiến thức từ JP1&JP2 về Comparator interface
 */
public class ApartmentComparators {

    /**
     * Sắp xếp theo building block
     */
    public static Comparator<Apartment> byBuildingBlock() {
        return Comparator.comparing(a -> a.getBuildingBlock() != null ? a.getBuildingBlock() : "");
    }

    /**
     * Sắp xếp theo floor number (tăng dần)
     */
    public static Comparator<Apartment> byFloorNumberAscending() {
        return (a1, a2) -> {
            Integer floor1 = a1.getFloorNumber() != null ? a1.getFloorNumber() : 0;
            Integer floor2 = a2.getFloorNumber() != null ? a2.getFloorNumber() : 0;
            return Integer.compare(floor1, floor2);
        };
    }

    /**
     * Sắp xếp theo floor number (giảm dần)
     */
    public static Comparator<Apartment> byFloorNumberDescending() {
        return (a1, a2) -> {
            Integer floor1 = a1.getFloorNumber() != null ? a1.getFloorNumber() : 0;
            Integer floor2 = a2.getFloorNumber() != null ? a2.getFloorNumber() : 0;
            return Integer.compare(floor2, floor1);
        };
    }

    /**
     * Sắp xếp theo apartment number
     */
    public static Comparator<Apartment> byApartmentNumber() {
        return Comparator.comparing(a -> a.getApartmentNo() != null ? a.getApartmentNo() : "");
    }

    /**
     * Sắp xếp theo area (tăng dần)
     */
    public static Comparator<Apartment> byAreaAscending() {
        return (a1, a2) -> {
            Double area1 = a1.getArea() != null ? a1.getArea() : 0.0;
            Double area2 = a2.getArea() != null ? a2.getArea() : 0.0;
            return Double.compare(area1, area2);
        };
    }

    /**
     * Sắp xếp theo area (giảm dần)
     */
    public static Comparator<Apartment> byAreaDescending() {
        return (a1, a2) -> {
            Double area1 = a1.getArea() != null ? a1.getArea() : 0.0;
            Double area2 = a2.getArea() != null ? a2.getArea() : 0.0;
            return Double.compare(area2, area1);
        };
    }

    /**
     * Sắp xếp theo status
     */
    public static Comparator<Apartment> byStatus() {
        return Comparator.comparing(a -> a.getStatus() != null ? a.getStatus() : "");
    }

    /**
     * Sắp xếp kết hợp: Building Block -> Floor -> Apartment Number
     */
    public static Comparator<Apartment> byBlockThenFloorThenNumber() {
        return byBuildingBlock()
                .thenComparing(byFloorNumberAscending())
                .thenComparing(byApartmentNumber());
    }
}








