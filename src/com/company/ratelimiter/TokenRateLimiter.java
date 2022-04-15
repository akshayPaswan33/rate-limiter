package com.company.ratelimiter;

public class TokenRateLimiter implements RateLimiter{

    private int token;
    private final int numRequestPerSec;
    private Long lastRefillTime;

    public TokenRateLimiter(int numRequestPerSec) {
        this.token = numRequestPerSec;
        this.numRequestPerSec = numRequestPerSec;
        lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public boolean allow() {
        synchronized (this) {
            refill();
            if(token==0) return false;
            token --;
            return true;
        }
    }

    private void refill() {
        Long currTime = System.currentTimeMillis();
        long secPassed = (currTime-lastRefillTime)/1000;

        if(secPassed > 0) {
            token = numRequestPerSec;
            lastRefillTime = System.currentTimeMillis();
        }
    }
}

//pros - memory efficient

// cons - need to lock this object in a distributed system
