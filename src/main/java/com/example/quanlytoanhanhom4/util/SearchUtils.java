package com.example.quanlytoanhanhom4.util;

import java.util.Comparator;
import java.util.List;

/**
 * Utility class chứa các thuật toán tìm kiếm từ JP1&JP2
 * Bao gồm: Linear Search, Binary Search
 */
public class SearchUtils {

    /**
     * Linear Search - Tìm kiếm tuyến tính
     * Độ phức tạp: O(n)
     * @return index của phần tử tìm thấy, -1 nếu không tìm thấy
     */
    public static <T> int linearSearch(List<T> list, T target) {
        if (list == null || target == null) {
            return -1;
        }
        
        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Linear Search với Comparator
     */
    public static <T> int linearSearch(List<T> list, T target, Comparator<T> comparator) {
        if (list == null || target == null) {
            return -1;
        }
        
        for (int i = 0; i < list.size(); i++) {
            if (comparator.compare(target, list.get(i)) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Binary Search - Tìm kiếm nhị phân
     * Độ phức tạp: O(log n)
     * Yêu cầu: Danh sách phải được sắp xếp
     * @return index của phần tử tìm thấy, -1 nếu không tìm thấy
     */
    public static <T extends Comparable<T>> int binarySearch(List<T> list, T target) {
        if (list == null || target == null || list.isEmpty()) {
            return -1;
        }
        
        int left = 0;
        int right = list.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midElement = list.get(mid);
            
            int comparison = target.compareTo(midElement);
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return -1;
    }

    /**
     * Binary Search với Comparator
     */
    public static <T> int binarySearch(List<T> list, T target, Comparator<T> comparator) {
        if (list == null || target == null || list.isEmpty()) {
            return -1;
        }
        
        int left = 0;
        int right = list.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midElement = list.get(mid);
            
            int comparison = comparator.compare(target, midElement);
            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return -1;
    }

    /**
     * Recursive Binary Search - Tìm kiếm nhị phân đệ quy
     */
    public static <T extends Comparable<T>> int binarySearchRecursive(List<T> list, T target) {
        if (list == null || target == null || list.isEmpty()) {
            return -1;
        }
        return binarySearchRecursive(list, target, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> int binarySearchRecursive(List<T> list, T target, int left, int right) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        T midElement = list.get(mid);
        
        int comparison = target.compareTo(midElement);
        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return binarySearchRecursive(list, target, left, mid - 1);
        } else {
            return binarySearchRecursive(list, target, mid + 1, right);
        }
    }

    /**
     * Recursive Binary Search với Comparator
     */
    public static <T> int binarySearchRecursive(List<T> list, T target, Comparator<T> comparator) {
        if (list == null || target == null || list.isEmpty()) {
            return -1;
        }
        return binarySearchRecursive(list, target, 0, list.size() - 1, comparator);
    }

    private static <T> int binarySearchRecursive(List<T> list, T target, int left, int right, Comparator<T> comparator) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        T midElement = list.get(mid);
        
        int comparison = comparator.compare(target, midElement);
        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return binarySearchRecursive(list, target, left, mid - 1, comparator);
        } else {
            return binarySearchRecursive(list, target, mid + 1, right, comparator);
        }
    }

    /**
     * Tìm kiếm tất cả các phần tử thỏa mãn điều kiện
     */
    public static <T> int[] findAllIndices(List<T> list, T target) {
        if (list == null || target == null) {
            return new int[0];
        }
        
        java.util.ArrayList<Integer> indices = new java.util.ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) {
                indices.add(i);
            }
        }
        
        return indices.stream().mapToInt(i -> i).toArray();
    }
}








