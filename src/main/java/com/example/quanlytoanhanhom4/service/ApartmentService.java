package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Apartment;
import com.example.quanlytoanhanhom4.util.DataStructureUtils;
import com.example.quanlytoanhanhom4.util.SearchUtils;
import com.example.quanlytoanhanhom4.util.SortingUtils;
import com.example.quanlytoanhanhom4.util.comparator.ApartmentComparators;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ApartmentService {

    public static List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM apartment ORDER BY COALESCE(building_block, ''), COALESCE(floor_number, 0), apartment_no";

        System.out.println("========================================");
        System.out.println("BẮT ĐẦU LOAD DỮ LIỆU CĂN HỘ");
        System.out.println("========================================");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("✓ Kết nối database thành công");
            System.out.println("✓ Đang thực thi query: " + sql);

            int count = 0;
            int errorCount = 0;
            while (rs.next()) {
                try {
                    Apartment apartment = mapResultSetToApartment(rs);
                    if (apartment != null && apartment.getApartmentNo() != null) {
                        apartments.add(apartment);
                        count++;
                        if (count <= 3) {
                            System.out.println("  [OK] Loaded: " + apartment.getApartmentNo() +
                                    " - Floor: " + apartment.getFloorNumber() +
                                    " - Block: " + apartment.getBuildingBlock() +
                                    " - Status: " + apartment.getStatus());
                        }
                    } else {
                        System.err.println("  [ERROR] Apartment null hoặc không có apartment_no tại row " + (count + errorCount + 1));
                        errorCount++;
                    }
                } catch (Exception e) {
                    System.err.println("  [ERROR] Lỗi khi map apartment tại row " + (count + errorCount + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                    errorCount++;
                }
            }
            System.out.println("========================================");
            System.out.println("KẾT QUẢ: Đã lấy được " + count + " căn hộ từ database");
            if (errorCount > 0) {
                System.err.println("CẢNH BÁO: Có " + errorCount + " lỗi khi load dữ liệu");
            }
            System.out.println("========================================");
        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("LỖI SQL khi lấy danh sách căn hộ: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("LỖI KHÔNG XÁC ĐỊNH: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        }
        return apartments;
    }

    public static List<Apartment> getApartmentsByStatus(String status) {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM apartment WHERE status = ? ORDER BY COALESCE(building_block, ''), COALESCE(floor_number, 0), apartment_no";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    Apartment apartment = mapResultSetToApartment(rs);
                    apartments.add(apartment);
                } catch (Exception e) {
                    System.err.println("Lỗi khi map apartment: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy căn hộ theo status: " + e.getMessage());
            e.printStackTrace();
        }
        return apartments;
    }

    public static Apartment getApartmentById(int id) {
        String sql = "SELECT * FROM apartment WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToApartment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addApartment(Apartment apartment) {
        String sql = "INSERT INTO apartment (apartment_code, apartment_no, building_block, floor_number, " +
                "area, apartment_type, number_of_rooms, status, owner_id, max_occupants, " +
                "internal_notes, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String apartmentCode = apartment.getApartmentCode() != null ? 
                apartment.getApartmentCode() : apartment.getApartmentNo();
            pstmt.setString(1, apartmentCode);
            pstmt.setString(2, apartment.getApartmentNo() != null ? 
                apartment.getApartmentNo() : apartmentCode);
            pstmt.setString(3, apartment.getBuildingBlock());
            pstmt.setObject(4, apartment.getFloorNumber());
            pstmt.setObject(5, apartment.getArea());
            pstmt.setString(6, apartment.getApartmentType());
            pstmt.setObject(7, apartment.getNumberOfRooms());
            pstmt.setString(8, apartment.getStatus() != null ? apartment.getStatus() : "ĐỂ_TRỐNG");
            pstmt.setObject(9, apartment.getOwnerId());
            pstmt.setObject(10, apartment.getMaxOccupants());
            String internalNotes = apartment.getInternalNotes() != null ? 
                apartment.getInternalNotes() : apartment.getNotes();
            pstmt.setString(11, internalNotes);
            pstmt.setString(12, apartment.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateApartment(Apartment apartment) {
        String sql = "UPDATE apartment SET apartment_code = ?, apartment_no = ?, building_block = ?, " +
                "floor_number = ?, area = ?, apartment_type = ?, number_of_rooms = ?, status = ?, " +
                "owner_id = ?, max_occupants = ?, internal_notes = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String apartmentCode = apartment.getApartmentCode() != null ? 
                apartment.getApartmentCode() : apartment.getApartmentNo();
            pstmt.setString(1, apartmentCode);
            pstmt.setString(2, apartment.getApartmentNo() != null ? 
                apartment.getApartmentNo() : apartmentCode);
            pstmt.setString(3, apartment.getBuildingBlock());
            pstmt.setObject(4, apartment.getFloorNumber());
            pstmt.setObject(5, apartment.getArea());
            pstmt.setString(6, apartment.getApartmentType());
            pstmt.setObject(7, apartment.getNumberOfRooms());
            pstmt.setString(8, apartment.getStatus() != null ? apartment.getStatus() : "ĐỂ_TRỐNG");
            pstmt.setObject(9, apartment.getOwnerId());
            pstmt.setObject(10, apartment.getMaxOccupants());
            String internalNotes = apartment.getInternalNotes() != null ? 
                apartment.getInternalNotes() : apartment.getNotes();
            pstmt.setString(11, internalNotes);
            pstmt.setString(12, apartment.getNotes());
            pstmt.setInt(13, apartment.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteApartment(int id) {
        String sql = "DELETE FROM apartment WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Apartment mapResultSetToApartment(ResultSet rs) throws SQLException {
        Apartment apartment = new Apartment();
        apartment.setId(rs.getInt("id"));
        
        // Đọc apartment_code hoặc apartment_no
        String apartmentCode = null;
        try {
            apartmentCode = rs.getString("apartment_code");
        } catch (SQLException e) {
            // Column không tồn tại, dùng apartment_no
        }
        if (apartmentCode == null) {
            apartmentCode = rs.getString("apartment_no");
        }
        apartment.setApartmentCode(apartmentCode);
        apartment.setApartmentNo(rs.getString("apartment_no"));
        
        apartment.setBuildingBlock(rs.getString("building_block"));
        
        Integer floorNumber = rs.getObject("floor_number", Integer.class);
        apartment.setFloorNumber(floorNumber);
        
        Double area = rs.getObject("area", Double.class);
        apartment.setArea(area);
        
        // Đọc apartment_type
        try {
            apartment.setApartmentType(rs.getString("apartment_type"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        Integer numberOfRooms = rs.getObject("number_of_rooms", Integer.class);
        apartment.setNumberOfRooms(numberOfRooms);
        
        String status = rs.getString("status");
        apartment.setStatus(status != null ? status : "ĐỂ_TRỐNG");
        
        // Đọc owner_id nếu có
        try {
            Integer ownerId = rs.getObject("owner_id", Integer.class);
            apartment.setOwnerId(ownerId);
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
            apartment.setOwnerId(null);
        }
        
        // Đọc max_occupants
        try {
            Integer maxOccupants = rs.getObject("max_occupants", Integer.class);
            apartment.setMaxOccupants(maxOccupants);
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        // Đọc internal_notes
        try {
            String internalNotes = rs.getString("internal_notes");
            apartment.setInternalNotes(internalNotes);
        } catch (SQLException e) {
            // Column không tồn tại, dùng notes
        }
        
        apartment.setNotes(rs.getString("notes"));

        // Kiểm tra và đọc created_at nếu có
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                apartment.setCreatedAt(createdAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        // Kiểm tra và đọc updated_at nếu có
        try {
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                apartment.setUpdatedAt(updatedAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        return apartment;
    }

    // ========== CÁC PHƯƠNG THỨC NÂNG CAO SỬ DỤNG CẤU TRÚC DỮ LIỆU & GIẢI THUẬT TỪ JP1&JP2 ==========

    /**
     * Lấy apartments và sắp xếp theo building block, floor, number sử dụng Quick Sort
     * Áp dụng thuật toán Quick Sort từ JP1&JP2
     */
    public static List<Apartment> getAllApartmentsSorted() {
        List<Apartment> apartments = getAllApartments();
        SortingUtils.quickSort(apartments, ApartmentComparators.byBlockThenFloorThenNumber());
        return apartments;
    }

    /**
     * Lấy apartments và sắp xếp theo diện tích sử dụng Merge Sort
     * Áp dụng thuật toán Merge Sort từ JP1&JP2
     */
    public static List<Apartment> getAllApartmentsSortedByArea() {
        List<Apartment> apartments = getAllApartments();
        SortingUtils.mergeSort(apartments, ApartmentComparators.byAreaDescending());
        return apartments;
    }

    /**
     * Tìm apartment theo ID sử dụng Binary Search (yêu cầu danh sách đã sắp xếp)
     * Áp dụng thuật toán Binary Search từ JP1&JP2
     */
    public static Apartment findApartmentByIdBinarySearch(List<Apartment> sortedApartments, Integer apartmentId) {
        if (sortedApartments == null || sortedApartments.isEmpty() || apartmentId == null) {
            return null;
        }
        
        Apartment searchKey = new Apartment();
        searchKey.setId(apartmentId);
        
        int index = SearchUtils.binarySearch(sortedApartments, searchKey, 
            Comparator.comparing(Apartment::getId, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? sortedApartments.get(index) : null;
    }

    /**
     * Tìm apartment theo apartment number sử dụng Linear Search
     * Áp dụng thuật toán Linear Search từ JP1&JP2
     */
    public static Apartment findApartmentByNumberLinearSearch(String apartmentNumber) {
        List<Apartment> apartments = getAllApartments();
        Apartment searchKey = new Apartment();
        searchKey.setApartmentNo(apartmentNumber);
        
        int index = SearchUtils.linearSearch(apartments, searchKey, 
            Comparator.comparing(Apartment::getApartmentNo, Comparator.nullsLast(Comparator.naturalOrder())));
        
        return index >= 0 ? apartments.get(index) : null;
    }

    /**
     * Nhóm apartments theo status sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<String, List<Apartment>> groupApartmentsByStatus() {
        List<Apartment> apartments = getAllApartments();
        return DataStructureUtils.groupBy(apartments, Apartment::getStatus);
    }

    /**
     * Nhóm apartments theo building block sử dụng HashMap
     */
    public static Map<String, List<Apartment>> groupApartmentsByBlock() {
        List<Apartment> apartments = getAllApartments();
        return DataStructureUtils.groupBy(apartments, Apartment::getBuildingBlock);
    }

    /**
     * Nhóm apartments theo floor sử dụng HashMap
     */
    public static Map<Integer, List<Apartment>> groupApartmentsByFloor() {
        List<Apartment> apartments = getAllApartments();
        Map<Integer, List<Apartment>> grouped = new HashMap<>();
        
        for (Apartment apartment : apartments) {
            Integer floor = apartment.getFloorNumber() != null ? apartment.getFloorNumber() : 0;
            grouped.computeIfAbsent(floor, k -> new ArrayList<>()).add(apartment);
        }
        
        return grouped;
    }

    /**
     * Tạo Map từ apartments với key là apartment ID sử dụng HashMap
     * Áp dụng HashMap từ JP1&JP2
     */
    public static Map<Integer, Apartment> getApartmentsMapById() {
        List<Apartment> apartments = getAllApartments();
        return DataStructureUtils.listToMap(apartments, Apartment::getId);
    }

    /**
     * Tạo TreeMap từ apartments sắp xếp theo building block
     * Áp dụng TreeMap từ JP1&JP2
     */
    public static Map<String, List<Apartment>> getApartmentsTreeMapByBlock() {
        List<Apartment> apartments = getAllApartments();
        Map<String, List<Apartment>> grouped = DataStructureUtils.groupBy(apartments, Apartment::getBuildingBlock);
        return new TreeMap<>(grouped);
    }

    /**
     * Đếm số lượng apartments theo status sử dụng HashMap
     */
    public static Map<String, Integer> countApartmentsByStatus() {
        List<Apartment> apartments = getAllApartments();
        Map<String, Integer> countMap = new HashMap<>();
        
        for (Apartment apartment : apartments) {
            String status = apartment.getStatus() != null ? apartment.getStatus() : "UNKNOWN";
            countMap.put(status, countMap.getOrDefault(status, 0) + 1);
        }
        
        return countMap;
    }

    /**
     * Đếm số lượng apartments theo building block sử dụng HashMap
     */
    public static Map<String, Integer> countApartmentsByBlock() {
        List<Apartment> apartments = getAllApartments();
        Map<String, Integer> countMap = new HashMap<>();
        
        for (Apartment apartment : apartments) {
            String block = apartment.getBuildingBlock() != null ? apartment.getBuildingBlock() : "UNKNOWN";
            countMap.put(block, countMap.getOrDefault(block, 0) + 1);
        }
        
        return countMap;
    }

    /**
     * Tìm apartment có diện tích lớn nhất sử dụng thuật toán tìm max
     */
    public static Apartment findLargestApartment() {
        List<Apartment> apartments = getAllApartments();
        if (apartments.isEmpty()) {
            return null;
        }
        return DataStructureUtils.findMax(apartments, ApartmentComparators.byAreaDescending());
    }

    /**
     * Tìm apartment có diện tích nhỏ nhất sử dụng thuật toán tìm min
     */
    public static Apartment findSmallestApartment() {
        List<Apartment> apartments = getAllApartments();
        if (apartments.isEmpty()) {
            return null;
        }
        return DataStructureUtils.findMin(apartments, ApartmentComparators.byAreaAscending());
    }

    /**
     * Lấy apartments trống sử dụng HashSet để loại bỏ trùng lặp
     */
    public static Set<Apartment> getEmptyApartmentsSet() {
        List<Apartment> apartments = getAllApartments();
        Set<Apartment> emptySet = new HashSet<>();
        
        for (Apartment apartment : apartments) {
            if ("ĐỂ_TRỐNG".equals(apartment.getStatus())) {
                emptySet.add(apartment);
            }
        }
        
        return emptySet;
    }

    /**
     * Lấy apartments sắp xếp theo TreeSet (theo apartment number)
     */
    public static Set<Apartment> getApartmentsTreeSet() {
        List<Apartment> apartments = getAllApartments();
        return DataStructureUtils.listToTreeSet(apartments, ApartmentComparators.byApartmentNumber());
    }

    /**
     * Lấy apartments theo floor sử dụng PriorityQueue (ưu tiên tầng cao)
     */
    public static List<Apartment> getApartmentsByFloorPriority(int floor) {
        List<Apartment> apartments = getAllApartments();
        List<Apartment> floorApartments = apartments.stream()
            .filter(a -> a.getFloorNumber() != null && a.getFloorNumber().equals(floor))
            .collect(Collectors.toList());
        
        PriorityQueue<Apartment> pq = DataStructureUtils.listToPriorityQueue(
            floorApartments, ApartmentComparators.byApartmentNumber());
        
        List<Apartment> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }
        return result;
    }
}

