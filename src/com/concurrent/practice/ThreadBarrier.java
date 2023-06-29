package com.concurrent.practice;

import java.util.Objects;

public class ThreadBarrier {
    private final int count;
    private int waiting;
    private final Runnable callback;
    private boolean isBroken;
    private int released;

    public ThreadBarrier(int count, Runnable callback) {
        this.count = count;
        waiting = 0;
        released = 0;
        this.callback = callback;
        isBroken = false;
    }

    public ThreadBarrier(int count) {
        this(count, null);
    }

    public synchronized void await() throws InterruptedException, BrokenBarrierException {
        if (isBroken) {
            throw new BrokenBarrierException("Barrier was already broken when Thread : " +
                    Thread.currentThread().getId() + " attempted to await on the barrier");
        }

        ++waiting;
        if (count == waiting) {
            if (Objects.nonNull(callback)) {
                callback.run();
            }
            isBroken = true;
            notifyAll();
        }

        while (!isBroken) {
            wait();
        }

        ++released;
        if (released == count) {
            // reset barrier
            released = 0;
            waiting = 0;
            isBroken = false;
        }
    }

    class BrokenBarrierException extends Throwable {
        public BrokenBarrierException(String message) {
            super(message);
        }
    }
}
