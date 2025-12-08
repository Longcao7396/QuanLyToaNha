package com.example.quanlytoanhanhom4.util;

import java.util.Comparator;
import java.util.List;

/**
 * Utility class chứa các thuật toán sắp xếp từ JP1&JP2
 * Bao gồm: Bubble Sort, Selection Sort, Insertion Sort, Quick Sort, Merge Sort
 */
public class SortingUtils {

    /**
     * Bubble Sort - Sắp xếp nổi bọt
     * Độ phức tạp: O(n²)
     */
    public static <T extends Comparable<T>> void bubbleSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    // Swap elements
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            // Nếu không có swap nào, danh sách đã được sắp xếp
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Bubble Sort với Comparator
     */
    public static <T> void bubbleSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    /**
     * Selection Sort - Sắp xếp chọn
     * Độ phức tạp: O(n²)
     */
    public static <T extends Comparable<T>> void selectionSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j).compareTo(list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            // Swap elements
            if (minIndex != i) {
                T temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    /**
     * Selection Sort với Comparator
     */
    public static <T> void selectionSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                T temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    /**
     * Insertion Sort - Sắp xếp chèn
     * Độ phức tạp: O(n²) worst case, O(n) best case
     */
    public static <T extends Comparable<T>> void insertionSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            
            // Di chuyển các phần tử lớn hơn key về phía sau
            while (j >= 0 && list.get(j).compareTo(key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    /**
     * Insertion Sort với Comparator
     */
    public static <T> void insertionSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    /**
     * Quick Sort - Sắp xếp nhanh
     * Độ phức tạp: O(n log n) average, O(n²) worst case
     */
    public static <T extends Comparable<T>> void quickSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        quickSort(list, 0, list.size() - 1);
    }

    private static <T extends Comparable<T>> void quickSort(List<T> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(List<T> list, int low, int high) {
        T pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        
        return i + 1;
    }

    /**
     * Quick Sort với Comparator
     */
    public static <T> void quickSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        
        return i + 1;
    }

    /**
     * Merge Sort - Sắp xếp trộn
     * Độ phức tạp: O(n log n)
     */
    public static <T extends Comparable<T>> void mergeSort(List<T> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        mergeSort(list, 0, list.size() - 1);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void mergeSort(List<T> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(List<T> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        Object[] leftArray = new Object[n1];
        Object[] rightArray = new Object[n2];
        
        for (int i = 0; i < n1; i++) {
            leftArray[i] = list.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = list.get(mid + 1 + j);
        }
        
        int i = 0, j = 0;
        int k = left;
        
        while (i < n1 && j < n2) {
            if (((T) leftArray[i]).compareTo((T) rightArray[j]) <= 0) {
                list.set(k, (T) leftArray[i]);
                i++;
            } else {
                list.set(k, (T) rightArray[j]);
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            list.set(k, (T) leftArray[i]);
            i++;
            k++;
        }
        
        while (j < n2) {
            list.set(k, (T) rightArray[j]);
            j++;
            k++;
        }
    }

    /**
     * Merge Sort với Comparator
     */
    public static <T> void mergeSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return;
        }
        mergeSort(list, 0, list.size() - 1, comparator);
    }

    @SuppressWarnings("unchecked")
    private static <T> void mergeSort(List<T> list, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid, comparator);
            mergeSort(list, mid + 1, right, comparator);
            merge(list, left, mid, right, comparator);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void merge(List<T> list, int left, int mid, int right, Comparator<T> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        Object[] leftArray = new Object[n1];
        Object[] rightArray = new Object[n2];
        
        for (int i = 0; i < n1; i++) {
            leftArray[i] = list.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = list.get(mid + 1 + j);
        }
        
        int i = 0, j = 0;
        int k = left;
        
        while (i < n1 && j < n2) {
            if (comparator.compare((T) leftArray[i], (T) rightArray[j]) <= 0) {
                list.set(k, (T) leftArray[i]);
                i++;
            } else {
                list.set(k, (T) rightArray[j]);
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            list.set(k, (T) leftArray[i]);
            i++;
            k++;
        }
        
        while (j < n2) {
            list.set(k, (T) rightArray[j]);
            j++;
            k++;
        }
    }
}








