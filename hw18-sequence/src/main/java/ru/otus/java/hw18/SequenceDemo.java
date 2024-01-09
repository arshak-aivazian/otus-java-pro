package ru.otus.java.hw18;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SequenceDemo {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new SequenceThread(1, lock, condition));
        executorService.execute(new SequenceThread(2, lock, condition));

        Thread.sleep(100);

        executorService.shutdownNow();
    }


    private static class SequenceThread implements Runnable {
        private static int currentThreadId = 1;
        private static int count = 1;

        private final int threadId;
        private final Lock lock;
        private final Condition condition;

        private boolean increase;

        private SequenceThread(int threadId, Lock lock, Condition condition) {
            this.threadId = threadId;
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                if (count == 1) {
                    increase = true;
                }

                lock.lock();

                try {
                    while (threadId != currentThreadId) {
                        condition.await();
                    }

                    System.out.println(String.format("Поток %s: %s", threadId, count));

                    if (threadId == 1) {
                        currentThreadId = 2;
                    } else if (threadId == 2) {
                        if (increase) {
                            count++;
                        } else {
                            count--;
                        }
                        currentThreadId = 1;
                    }

                    condition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

                if (count == 10) {
                    increase = false;
                }
            }
        }
    }
}
