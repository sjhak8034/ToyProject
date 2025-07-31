package com.example.toyproject.common.reentrantLock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class KeyLockManager {
    private static ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public boolean tryLock(String key, long timeout, TimeUnit unit) throws InterruptedException {
        ReentrantLock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        return lock.tryLock(timeout, unit);  // 지정된 시간만 기다리고 실패하면 false
    }

    public void unlock(String key) {
        ReentrantLock lock = lockMap.get(key);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

}