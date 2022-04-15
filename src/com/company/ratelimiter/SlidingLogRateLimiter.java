package com.company.ratelimiter;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingLogRateLimiter implements RateLimiter{

    private final int numReqPerSec;

    private final Queue<Long> log;

    public SlidingLogRateLimiter(int numReqPerSec) {
        this.numReqPerSec = numReqPerSec;
        log = new LinkedList<>();
    }

    @Override
    public boolean allow() {
        long currTime = System.currentTimeMillis();
        Long boundary = currTime - 1000;
        synchronized (log) {
            while (!log.isEmpty() && log.peek() <= boundary) log.poll();
            log.offer(currTime);
            return log.size() <= numReqPerSec;
        }
    }
}

//pros - requests near boundary won't be served in excess

//cons - not memory efficient
