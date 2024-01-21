package ru.otus.java;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleMedianListImpl implements MedianList<Double> {
    private final PriorityQueue<Double> minHeap = new PriorityQueue<>();
    private final PriorityQueue<Double> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    private final Lock lock = new ReentrantLock();

    @Override
    public int size() {
        lock.lock();
        try {
            return minHeap.size() + maxHeap.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(Double item) {
        lock.lock();
        System.out.println("adding " + item);
        try {
            if (maxHeap.size() == minHeap.size()) {
                minHeap.add(item);
                maxHeap.add(minHeap.remove());
            } else {
                maxHeap.add(item);
                minHeap.add(maxHeap.remove());
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void remove(Double item) {
        lock.lock();
        try {
            minHeap.remove(item);
            maxHeap.remove(item);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public double getMedian() {
        lock.lock();
        try {
            return maxHeap.size() == minHeap.size()
                    ? (maxHeap.peek() + minHeap.peek()) / 2.0
                    : maxHeap.peek() / 1.0;
        } finally {
            lock.unlock();
        }
    }
}
