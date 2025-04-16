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
        while (!pool.isTerminated()) {

        }
        calls.sort(Comparator.comparing(CallEntity::getStartCall));
        HashMap<String, List<CallEntity>> cdrMap = new HashMap<>();
        for (SubscriberEntity sub : allSubs) {
            cdrMap.put(sub.getMsisdn(), new ArrayList<>());
        }
        int approvedCalls = 0;
        for (CallEntity c : calls) {
            List<CallEntity> initList = cdrMap.getOrDefault(c.getInitiating().getMsisdn(), new ArrayList<>());
            if (validatedCall(initList, c)) {
                continue;
            }
            List<CallEntity> recList = cdrMap.getOrDefault(c.getReceiving().getMsisdn(), new ArrayList<>());
            if (validatedCall(recList, c)) {
                continue;
            }
            approvedCalls++;

            if (c.getStartCall().getDayOfYear() != c.getEndCall().getDayOfYear()) {
                LocalDateTime ldtEnd = c.getEndCall();
                LocalDateTime midnight = ldtEnd.toLocalDate().atStartOfDay();

                CallEntity beforeMidnight = new CallEntity(null, c.getInitiating(), c.getReceiving(), c.getStartCall(), midnight);
                processSingleCall(beforeMidnight, initList, recList, cdrMap);

                CallEntity afterMidnight = new CallEntity(null, c.getInitiating(), c.getReceiving(), midnight, c.getEndCall());
                processSingleCall(afterMidnight, initList, recList, cdrMap);

            } else {
                processSingleCall(c, initList, recList, cdrMap);
            }

            callRepo.saveAndFlush(c);


        }
        return approvedCalls;
    }

    private void processSingleCall(CallEntity call, List<CallEntity> initList, List<CallEntity> recList, Map<String, List<CallEntity>> cdrMap) {
        initList.add(call);
        recList.add(call);

        checkAndSendReport(call, recList, initList);


    }

    private void checkAndSendReport(CallEntity call, List<CallEntity> initList, List<CallEntity> recList) {
        if (initList.size() == 10) {
            cdrService.sendCdrReport(call.getInitiating().getMsisdn(), initList);
            initList.clear();

        }
        if (recList.size() == 10) {
            cdrService.sendCdrReport(call.getReceiving().getMsisdn(), recList);
            recList.clear();
        }
    }

    boolean validatedCall(List<CallEntity> callList, CallEntity call) {
        return !callList.isEmpty() && callList.get(callList.size() - 1).getEndCall().isAfter(call.getStartCall());
    }

}
//TODO: Напиши человеческие юниты, вроде на данный момент все работает,
// но проверь что в brt улетают все звонки что должны улететь
// переписать валидацию
