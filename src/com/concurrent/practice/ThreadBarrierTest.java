package com.concurrent.practice;

import java.util.ArrayList;
import java.util.List;

public class ThreadBarrierTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadBarrier barrierWithoutCallback = new ThreadBarrier(5);
        ThreadBarrier barrierWithCallback = new ThreadBarrier(5,
                () -> System.out.println("Callback invoked by thread : " + Thread.currentThread().getName()));

        // test normal barrier
        for (int i = 0; i < 5; ++i) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread : " + Thread.currentThread().getName() + " calling await");
                try {
                    barrierWithoutCallback.await();
                    System.out.println("Barrier was broken");
                } catch (InterruptedException | ThreadBarrier.BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName("thread: "+i);
            thread.start();
        }

        // gap between two invocations to prevent interleaving outputs
        Thread.sleep(2000);

        // test barrier with callback
        for (int i = 0; i < 5; ++i) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread : " + Thread.currentThread().getName() + " calling await");
                try {
                    barrierWithCallback.await();
                    System.out.println("Barrier was broken");
                } catch (InterruptedException | ThreadBarrier.BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName("thread: "+i);
            thread.start();
        }

        // gap between two invocations to prevent interleaving outputs
        Thread.sleep(2000);

        // test exception on broken barrier
        for (int i = 0; i < 10; ++i) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread : " + Thread.currentThread().getName() + " calling await");
                try {
                    barrierWithoutCallback.await();
                    System.out.println("Barrier was broken");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ThreadBarrier.BrokenBarrierException e) {
                    System.out.println(e.getMessage());
                }
            });
            thread.setName("thread: "+i);
            thread.start();
        }
    }
}
