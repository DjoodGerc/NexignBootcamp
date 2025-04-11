package com.example.demo.services;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.repository.CallRepo;
import com.example.demo.repository.SubsRepo;
import com.example.demo.task.GenerateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * служебный класс для генерации
 * generateSubs(int nSubs) - метод для генерации абонентов (и сохранения в бд) (можно добавить генерацию с реальными кодами операторов)
 * generateCdr() - метод для генерации cdr (и сохранения в бд) (случайное кол-во записей за год (от 1 до 5000 /можно вынести в параметр метода/))
 * <p>
 * !!! заметил, что абоненты могут генерироваться не случайные, не успею поправить.
 */
@Service
public class GenerationService {
    @Autowired
    SubsRepo subsRepo;
    @Autowired
    CdrService cdrService;
    @Autowired
    CallRepo callRepo;
    Random random = new Random();


    public int generateCalls(int bot, int top) {
        List<SubscriberEntity> subsList = subsRepo.findAll();
        List<CallEntity> calls = Collections.synchronizedList(new ArrayList<>());

        List<SubscriberEntity> allSubs = subsRepo.findAll();
        List<SubscriberEntity> romashkaSubs = subsRepo.findByOperator_Id(1);
        long startLong = LocalDateTime.now().minusYears(1).minusDays(1).toEpochSecond(ZoneOffset.UTC);
        long endLong = LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC);
        int nCalls = random.nextInt(bot, top);
        int nPools = 5;
        ExecutorService pool = Executors.newFixedThreadPool(nPools);
        for (int i = 0; i < nPools; i++) {
            if (nCalls % nPools != 0 && i == nPools - 1) {
                pool.execute(new GenerateTask(nCalls % nPools, calls, startLong, endLong, allSubs, romashkaSubs));
            } else {
                pool.execute(new GenerateTask(nCalls / nPools, calls, startLong, endLong, allSubs, romashkaSubs));


            }
        }

        pool.shutdown();
        while (!pool.isTerminated()) {

        }
        calls.sort(Comparator.comparing(CallEntity::getStartCall));
        HashMap<String, List<CallEntity>> cdrMap = new HashMap<>();
        for (SubscriberEntity sub: allSubs) {
            cdrMap.put(sub.getMsisdn(),new ArrayList<>());
        }
        int nApprovedCalls=0;
        int notAprr=0;
        for (CallEntity c:calls) {
            List<CallEntity> initList = cdrMap.getOrDefault(c.getInitiating().getMsisdn(), new ArrayList<>());
            if (!initList.isEmpty()) {
                if (initList.get(initList.size() - 1).getEndCall().after(c.getStartCall())) {
                    notAprr++;
                    continue;
                }
            }
            List<CallEntity> recList = cdrMap.getOrDefault(c.getReceiving().getMsisdn(), new ArrayList<>());
            if (!recList.isEmpty()) {
                if (recList.get(recList.size() - 1).getEndCall().after(c.getStartCall())) {
                    notAprr++;
                    continue;
                }
            }
            nApprovedCalls++;

            if (c.getStartCall().toLocalDateTime().getDayOfYear()!=c.getEndCall().toLocalDateTime().getDayOfYear()){
                LocalDateTime ldtStart=c.getStartCall().toLocalDateTime();
                LocalDateTime ldtEnd=c.getEndCall().toLocalDateTime();
                LocalDateTime zero=ldtEnd.toLocalDate().atStartOfDay();
                CallEntity underZeroCall=new CallEntity(null,c.getInitiating(),c.getReceiving(),c.getStartCall(),Timestamp.valueOf(zero));
                initList.add(underZeroCall);
                recList.add(underZeroCall);

                if (initList.size() == 10 && underZeroCall.getInitiating().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(underZeroCall.getInitiating().getMsisdn(), initList);
                    cdrMap.put(underZeroCall.getInitiating().getMsisdn(), new ArrayList<>());

                }
                if (recList.size() == 10 && underZeroCall.getReceiving().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(underZeroCall.getReceiving().getMsisdn(), recList);
                    cdrMap.put(underZeroCall.getReceiving().getMsisdn(), new ArrayList<>());
                }
                CallEntity upperZeroCall=new CallEntity(null,c.getInitiating(),c.getReceiving(),Timestamp.valueOf(zero),c.getEndCall());
                initList.add(upperZeroCall);
                recList.add(upperZeroCall);
                if (initList.size() == 10 && upperZeroCall.getInitiating().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(upperZeroCall.getInitiating().getMsisdn(), initList);
                    cdrMap.put(upperZeroCall.getInitiating().getMsisdn(), new ArrayList<>());

                }
                if (recList.size() == 10 && upperZeroCall.getReceiving().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(upperZeroCall.getReceiving().getMsisdn(), recList);
                    cdrMap.put(upperZeroCall.getReceiving().getMsisdn(), new ArrayList<>());
                }

            }
            else{
                initList.add(c);
                recList.add(c);

                if (initList.size() == 10 && c.getInitiating().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(c.getInitiating().getMsisdn(), initList);
                    cdrMap.put(c.getInitiating().getMsisdn(), new ArrayList<>());

                }
                if (recList.size() == 10 && c.getReceiving().getOperator().getId() == 1) {
                    cdrService.sendCdrReport(c.getReceiving().getMsisdn(), recList);
                    cdrMap.put(c.getReceiving().getMsisdn(), new ArrayList<>());
                }
            }
            callRepo.saveAndFlush(c);



        }
        return nApprovedCalls;
    }

    @Deprecated
    public List<CallEntity> generateCallsDepr(int bot, int top) {
        List<CallEntity> cdrEntities = new ArrayList<>();
        List<SubscriberEntity> subscriberEntityList = subsRepo.findAll();
        List<SubscriberEntity> romashkaSubs = subsRepo.findByOperator_Id(1);
        long startTimestamp = LocalDateTime.now().minusYears(1).minusDays(1).toEpochSecond(ZoneOffset.UTC);
        long endTimestamp = LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC);


        int nCalls = random.nextInt(bot, top);
        List<Timestamp> startList = new ArrayList<>();
        for (int j = 0; j < nCalls; j++) {
            long randomTimestamp = startTimestamp + (long) (random.nextDouble() * (endTimestamp - startTimestamp));
            startList.add(Timestamp.from(Instant.ofEpochSecond(randomTimestamp)));
        }

        Collections.sort(startList);
        for (int i = 0; i < startList.size(); i++) {
            int s = random.nextInt(0, subscriberEntityList.size());
            long duration = random.nextLong(43200);
            Timestamp end = Timestamp.valueOf(startList.get(i).toLocalDateTime().plusSeconds(duration));
            CallEntity cdr;
            if (subscriberEntityList.get(s).getOperator().getId() == 1) {
                int r = (random.nextInt(1, subscriberEntityList.size()) + s) % subscriberEntityList.size();
                cdr = new CallEntity(null, subscriberEntityList.get(s), subscriberEntityList.get(r), startList.get(i), end);
            } else {
                int r = random.nextInt(romashkaSubs.size());
                cdr = new CallEntity(null, subscriberEntityList.get(s), romashkaSubs.get(r), startList.get(i), end);
            }


            cdr = callRepo.saveAndFlush(cdr);
            cdrEntities.add(cdr);
        }

        return cdrEntities;


    }
}
