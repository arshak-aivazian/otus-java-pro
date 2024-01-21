package ru.otus.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {

    public static void main(String[] args) throws InterruptedException {
        final MedianList<Double> medianList = new DoubleMedianListImpl();

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        long start = System.currentTimeMillis();

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            tasks.add(new Task(medianList, Double.valueOf(i)));
        }
        executorService.invokeAll(tasks);
        executorService.shutdown();

        System.out.println("time " + (System.currentTimeMillis() - start));
        System.out.println("median = " + medianList.getMedian());
    }

    private static class Task implements Callable<Double> {
        private final MedianList<Double> medianList;
        private final Double numberToAdd;

        private Task(MedianList<Double> medianList, Double numberToAdd) {
            this.medianList = medianList;
            this.numberToAdd = numberToAdd;
        }

        @Override
        public Double call() throws Exception {
            medianList.add(numberToAdd);
            return null;
        }
    }
}
