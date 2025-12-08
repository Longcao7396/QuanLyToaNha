package com.example.quanlytoanhanhom4.util;

import java.util.*;

/**
 * Utility class chứa các cấu trúc dữ liệu nâng cao từ JP1&JP2
 * Bao gồm: Stack, Queue, và các helper methods cho Collections
 */
public class DataStructureUtils {

    /**
     * Custom Stack implementation
     */
    public static class Stack<T> {
        private java.util.ArrayList<T> stack;

        public Stack() {
            this.stack = new java.util.ArrayList<>();
        }

        public void push(T item) {
            stack.add(item);
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.remove(stack.size() - 1);
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.get(stack.size() - 1);
        }

        public boolean isEmpty() {
            return stack.isEmpty();
        }

        public int size() {
            return stack.size();
        }

        public void clear() {
            stack.clear();
        }

        public List<T> toList() {
            return new java.util.ArrayList<>(stack);
        }
    }

    /**
     * Custom Queue implementation
     */
    public static class Queue<T> {
        private java.util.LinkedList<T> queue;

        public Queue() {
            this.queue = new java.util.LinkedList<>();
        }

        public void enqueue(T item) {
            queue.addLast(item);
        }

        public T dequeue() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue is empty");
            }
            return queue.removeFirst();
        }

        public T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("Queue is empty");
            }
            return queue.getFirst();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public int size() {
            return queue.size();
        }

        public void clear() {
            queue.clear();
        }

        public List<T> toList() {
            return new java.util.ArrayList<>(queue);
        }
    }

    /**
     * Helper method để tạo HashMap từ List với key extractor
     */
    public static <K, V> Map<K, V> listToMap(List<V> list, java.util.function.Function<V, K> keyExtractor) {
        Map<K, V> map = new HashMap<>();
        for (V value : list) {
            K key = keyExtractor.apply(value);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Helper method để tạo TreeMap từ List với key extractor và comparator
     */
    public static <K, V> Map<K, V> listToTreeMap(List<V> list, 
                                                   java.util.function.Function<V, K> keyExtractor,
                                                   Comparator<K> keyComparator) {
        Map<K, V> map = new TreeMap<>(keyComparator);
        for (V value : list) {
            K key = keyExtractor.apply(value);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Helper method để tạo HashSet từ List
     */
    public static <T> Set<T> listToHashSet(List<T> list) {
        return new HashSet<>(list);
    }

    /**
     * Helper method để tạo TreeSet từ List với comparator
     */
    public static <T> Set<T> listToTreeSet(List<T> list, Comparator<T> comparator) {
        TreeSet<T> set = new TreeSet<>(comparator);
        set.addAll(list);
        return set;
    }

    /**
     * Helper method để group list theo key
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> list, java.util.function.Function<V, K> keyExtractor) {
        Map<K, List<V>> grouped = new HashMap<>();
        for (V value : list) {
            K key = keyExtractor.apply(value);
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        return grouped;
    }

    /**
     * Helper method để tạo PriorityQueue từ List với comparator
     */
    public static <T> PriorityQueue<T> listToPriorityQueue(List<T> list, Comparator<T> comparator) {
        PriorityQueue<T> pq = new PriorityQueue<>(comparator);
        pq.addAll(list);
        return pq;
    }

    /**
     * Helper method để đảo ngược danh sách
     */
    public static <T> void reverseList(List<T> list) {
        int left = 0;
        int right = list.size() - 1;
        while (left < right) {
            T temp = list.get(left);
            list.set(left, list.get(right));
            list.set(right, temp);
            left++;
            right--;
        }
    }

    /**
     * Helper method để tìm phần tử lớn nhất
     */
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    /**
     * Helper method để tìm phần tử nhỏ nhất
     */
    public static <T extends Comparable<T>> T findMin(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min) < 0) {
                min = list.get(i);
            }
        }
        return min;
    }

    /**
     * Helper method để tìm phần tử lớn nhất với Comparator
     */
    public static <T> T findMax(List<T> list, Comparator<T> comparator) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(list.get(i), max) > 0) {
                max = list.get(i);
            }
        }
        return max;
    }

    /**
     * Helper method để tìm phần tử nhỏ nhất với Comparator
     */
    public static <T> T findMin(List<T> list, Comparator<T> comparator) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }
        T min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(list.get(i), min) < 0) {
                min = list.get(i);
            }
        }
        return min;
    }
}








