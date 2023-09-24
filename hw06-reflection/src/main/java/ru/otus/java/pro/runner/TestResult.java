package ru.otus.java.pro.runner;

public class TestResult {
    private int count;
    private int success;
    private int fail;

    public int getCount() {
        return count;
    }

    public int getSuccess() {
        return success;
    }

    public int getFail() {
        return fail;
    }

    public void incrementTestCount() {
        count++;
    }

    public void incrementSuccess() {
        success++;
    }

    public void incrementFail() {
        fail++;
    }
}
