package ru.otus.java.pro;

import ru.otus.java.pro.runner.CustomTestRunner;
import ru.otus.java.pro.runner.TestResult;
import ru.otus.java.pro.test.AnnotationDemoTest;

public class CustomTestApp {
    public static void main(String[] args) {
        CustomTestRunner testRunner = new CustomTestRunner();
        TestResult testResult = testRunner.run(AnnotationDemoTest.class);

        System.out.println("tests run - " + testResult.getCount());
        System.out.println("tests succeeded - " + testResult.getSuccess());
        System.out.println("tests failed - " + testResult.getFail());
    }
}
