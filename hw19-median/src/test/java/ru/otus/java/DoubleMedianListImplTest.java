package ru.otus.java;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoubleMedianListImplTest {

    @Test
    void getMedian() {
        DoubleMedianListImpl doubleMedianList = new DoubleMedianListImpl();

        doubleMedianList.add(10d);
        doubleMedianList.add(13d);
        doubleMedianList.add(14d);

        System.out.println(doubleMedianList.getMedian());
    }
}