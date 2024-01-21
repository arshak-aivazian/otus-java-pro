package ru.otus.java;

public interface MedianList<T extends Number> {
    int size();
    void add(T item);
    void remove(T item);
    double getMedian();
}
