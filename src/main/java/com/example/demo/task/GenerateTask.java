package com.example.demo.task;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;

public class GenerateTask implements Runnable {
    private List<CallEntity> calls;
    long startLong;
    long endLong;
    private List<SubscriberEntity> allSubs;
    private List<SubscriberEntity> romashkaSubs;
    Random random = new Random();
    int end;

    public GenerateTask(int end, List<CallEntity> calls, long startLong, long endLong, List<SubscriberEntity> allSubs, List<SubscriberEntity> romashkaSubs) {
        this.calls = calls;
        this.startLong = startLong;
        this.endLong = endLong;
        this.allSubs = allSubs;
        this.romashkaSubs = romashkaSubs;
        this.end=end;

    }


    @Override
    public void run() {
        for (int i = 0; i < end; i++) {
            long randomLong = startLong + (long) (random.nextDouble() * (endLong - startLong));
            Timestamp startTimestamp = Timestamp.from(Instant.ofEpochSecond(randomLong));
            int s = random.nextInt(0, allSubs.size());
            long duration = random.nextLong(86400);
            Timestamp end = Timestamp.valueOf(startTimestamp.toLocalDateTime().plusSeconds(duration));
            CallEntity call;
            if (allSubs.get(s).getOperator().getId() == 1) {
                int r = (random.nextInt(1, allSubs.size()) + s) % allSubs.size();
                call = new CallEntity(null, allSubs.get(s), allSubs.get(r), startTimestamp, end);
            } else {
                int r = random.nextInt(romashkaSubs.size());
                call = new CallEntity(null, allSubs.get(s), romashkaSubs.get(r), startTimestamp, end);
            }
            calls.add(call);
        }
    }
}
