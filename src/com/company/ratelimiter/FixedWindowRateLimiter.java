package com.company.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter{

    private final int numReqPerSec;
    private final ConcurrentMap<Long, AtomicInteger> map;

    public FixedWindowRateLimiter(int numReqPerSec) {
        this.numReqPerSec = numReqPerSec;
        map = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow() {
        Long key = System.currentTimeMillis()/1000;
        map.putIfAbsent(key,new AtomicInteger(0));
        int counter = map.get(key).incrementAndGet();
        return counter <= numReqPerSec;
    }
}

//pros - synchronized is not needed, thread safe

//cons - requests at the boundary may served more than required
// - need to remove old keys from map in a timely manner
