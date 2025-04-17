package com.example.demo.services;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.exception.DataAlreadyGeneratedException;
import com.example.demo.repository.CallRepo;
import com.example.demo.repository.SubsRepo;
import com.example.demo.task.GenerateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    @Value("${nPools}")
    int nPools;

    public int generateCalls(int bot, int top) throws DataAlreadyGeneratedException {

        if (callRepo.count() != 0) {
            throw new DataAlreadyGeneratedException("Data Already Generated",HttpStatus.ALREADY_REPORTED);
        }

        List<CallEntity> calls = Collections.synchronizedList(new ArrayList<>());

        List<SubscriberEntity> allSubs = subsRepo.findAll();

        long startLong = LocalDateTime.now().minusYears(1).minusDays(1).toEpochSecond(ZoneOffset.UTC);
        long endLong = LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC);
        int nCalls = random.nextInt(bot, top);
        ExecutorService pool = Executors.newFixedThreadPool(nPools);
        for (int i = 0; i < nPools; i++) {
            if (nCalls % nPools != 0 && i == nPools - 1) {
                pool.execute(new GenerateTask(nCalls % nPools, calls, startLong, endLong, allSubs));
            } else {
                pool.execute(new GenerateTask(nCalls / nPools, calls, startLong, endLong, allSubs));
            }
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }

        // Сортируем звонки по времени начала
        calls.sort(Comparator.comparing(CallEntity::getStartCall));

        // Используем ConcurrentHashMap для потокобезопасности
        ConcurrentHashMap<String, List<CallEntity>> cdrMap = new ConcurrentHashMap<>();
        allSubs.forEach(sub -> cdrMap.put(sub.getMsisdn(), new ArrayList<>()));

        int approvedCalls = 0;
        for (CallEntity call : calls) {
            String initMsisdn = call.getInitiating().getMsisdn();
            String recMsisdn = call.getReceiving().getMsisdn();

            List<CallEntity> initList = cdrMap.get(initMsisdn);
            List<CallEntity> recList = cdrMap.get(recMsisdn);

            if (validatedCall(initList, call) || validatedCall(recList, call)) {
                continue; // Пропускаем конфликтующие звонки
            }

            approvedCalls++;

            if (call.getStartCall().getDayOfYear() != call.getEndCall().getDayOfYear()) {
                LocalDateTime midnight = call.getEndCall().toLocalDate().atStartOfDay();

                CallEntity beforeMidnight = new CallEntity(
                        null, call.getInitiating(), call.getReceiving(),
                        call.getStartCall(), midnight
                );
                processSingleCall(beforeMidnight, cdrMap);

                CallEntity afterMidnight = new CallEntity(
                        null, call.getInitiating(), call.getReceiving(),
                        midnight, call.getEndCall()
                );
                processSingleCall(afterMidnight, cdrMap);
            } else {
                processSingleCall(call, cdrMap);
            }

            callRepo.saveAndFlush(call);
        }

        return approvedCalls;
    }



    private void processSingleCall(CallEntity call, ConcurrentHashMap<String, List<CallEntity>> cdrMap) {
        cdrMap.computeIfPresent(call.getInitiating().getMsisdn(), (k, v) -> {
            v.add(call);
            return v;
        });

        cdrMap.computeIfPresent(call.getReceiving().getMsisdn(), (k, v) -> {
            v.add(call);
            return v;
        });

        checkAndSendReport(call, cdrMap);
    }

    private void checkAndSendReport(CallEntity call, ConcurrentHashMap<String, List<CallEntity>> cdrMap) {
        String initMsisdn = call.getInitiating().getMsisdn();
        List<CallEntity> initList = cdrMap.get(initMsisdn);

        if (initList != null && initList.size() >= 10) {
            cdrService.sendCdrReport(initMsisdn, new ArrayList<>(initList));
            initList.clear();
        }

        String recMsisdn = call.getReceiving().getMsisdn();
        List<CallEntity> recList = cdrMap.get(recMsisdn);

        if (recList != null && recList.size() >= 10) {
            cdrService.sendCdrReport(recMsisdn, new ArrayList<>(recList));
            recList.clear();
        }
    }

    boolean validatedCall(List<CallEntity> callList, CallEntity call) {
        return callList.stream().anyMatch(existingCall ->
                existingCall.getEndCall().isAfter(call.getStartCall()) &&
                        existingCall.getStartCall().isBefore(call.getEndCall())
        );
    }


}
//TODO: Напиши человеческие юниты, вроде на данный момент все работает,
// но проверь что в brt улетают все звонки что должны улететь
// переписать валидацию
