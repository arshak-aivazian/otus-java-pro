package ru.otus.java.pro.serialization.csvstrategy;

import java.util.List;

public interface CsvParsingStrategy<T> {
    String[] getHeaders();
    List<String[]> getValues();
    T createObject(List<String[]> source);
}
