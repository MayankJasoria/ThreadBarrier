# Problem Statement
Implement a cyclic barrier which blocks all threads which call `await()` till a specified number of threads are blocked.
Once the number is reached, the barrier is `broken` and all threads are released. If a thread tries to call `await()` on
a broken barrier, a `BrokenBarrierException` is thrown. Once all blocked threads are released, the barrier is reset for 
use. The barrier should also support passing a callback function which is invoked by exactly 1 thread once the barrier
is broken.

## Solution approach
1. Maintain a boolean variable `isBroken`, initially set to `false`. Also maintain the count of thread to block, the
number of threads currently waiting, and the number of threads released.
2. make `await()` a synchronized method so that there is no contention for updating member variable values.
3. Add the condition check on `isBroken` at the start.
4. Increase the waiting threads count.
5. Check if number of waiting threads is equal to number of threads required to break the barrier. If so, invoke the 
callback if supplied, then set `isBroken` to `true` and `notifyAll()` waiting threads.
6. Increment the number of threads released count.
7. If the released count becomes equal to the count of threads requried to trip the barrier, reset all counters and 
`isBroken`

## Sample run
```
Thread : thread: 3 calling await
Thread : thread: 1 calling await
Thread : thread: 2 calling await
Thread : thread: 0 calling await
Thread : thread: 4 calling await
Barrier was broken
Barrier was broken
Barrier was broken
Barrier was broken
Barrier was broken
Thread : thread: 0 calling await
Thread : thread: 2 calling await
Thread : thread: 1 calling await
Thread : thread: 3 calling await
Thread : thread: 4 calling await
Callback invoked by thread : thread: 4
Barrier was broken
Barrier was broken
Barrier was broken
Barrier was broken
Barrier was broken
Thread : thread: 1 calling await
Thread : thread: 0 calling await
Thread : thread: 2 calling await
Thread : thread: 3 calling await
Thread : thread: 4 calling await
Thread : thread: 5 calling await
Thread : thread: 6 calling await
Barrier was broken
Thread : thread: 7 calling await
Thread : thread: 8 calling await
Thread : thread: 9 calling await
Barrier was already broken when Thread : 30 attempted to await on the barrier
Barrier was already broken when Thread : 34 attempted to await on the barrier
Barrier was already broken when Thread : 33 attempted to await on the barrier
Barrier was broken
Barrier was already broken when Thread : 32 attempted to await on the barrier
Barrier was already broken when Thread : 31 attempted to await on the barrier
Barrier was broken
Barrier was broken
Barrier was broken
```