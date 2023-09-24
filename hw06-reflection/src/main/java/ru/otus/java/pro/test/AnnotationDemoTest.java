package ru.otus.java.pro.test;

import ru.otus.java.pro.annotation.After;
import ru.otus.java.pro.annotation.Before;
import ru.otus.java.pro.annotation.Test;
import ru.otus.java.pro.exception.CustomTestException;

public class AnnotationDemoTest {

    @Before
    void beforeTest1() {
        System.out.println("before test 1");
    }

    @Before
    void beforeTest2() {
        System.out.println("before test 2");
    }

    @Test
    void successTest() {
        System.out.println("run test");
    }

    @Test
    void testThrowException() {
        throw new CustomTestException("throw my custom exception");
    }

    @After
    void afterTest1() {
        System.out.println("after test 1");
    }

    @After
    void afterTest2() {
        System.out.println("after test 2");
    }
}
