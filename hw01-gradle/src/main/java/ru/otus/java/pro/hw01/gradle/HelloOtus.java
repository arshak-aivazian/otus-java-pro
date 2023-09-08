package ru.otus.java.pro.hw01.gradle;

import com.google.common.collect.ImmutableList;

public class HelloOtus {
    public static void main(String[] args) {
        System.out.println("Immutable list from guava");
        System.out.println(ImmutableList.of(1, 2, 3, 4, 5));
    }
}
