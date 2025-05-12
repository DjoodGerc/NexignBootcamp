package com.example.demo.task;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class GenerateTask implements Runnable {
    private final List<CallEntity> calls;
    long startLong;
    long endLong;
    private final List<SubscriberEntity> allSubs;
    Random random = new Random();
    int end;

    public GenerateTask(int end, List<CallEntity> calls, long startLong, long endLong, List<SubscriberEntity> allSubs) {
        this.calls = calls;
        this.startLong = startLong;
        this.endLong = endLong;
        this.allSubs = allSubs;
        this.end = end;

    }


    @Override
    public void run() {
        for (int i = 0; i < end; i++) {
            long randomLong = startLong + (long) (random.nextDouble() * (endLong - startLong));
            LocalDateTime startTimestamp = LocalDateTime.ofEpochSecond(randomLong, 0, ZoneOffset.UTC);
            int s = random.nextInt(0, allSubs.size());
            long duration = random.nextLong(43200);
            LocalDateTime end = startTimestamp.plusSeconds(duration);
            CallEntity call;
            int r = (random.nextInt(1, allSubs.size()) + s) % allSubs.size();
            call = new CallEntity(null, allSubs.get(s), allSubs.get(r), startTimestamp, end);
            calls.add(call);
        }
    }
}
//exception handler brtApp.controller advice
