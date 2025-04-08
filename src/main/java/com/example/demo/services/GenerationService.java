package com.example.demo.services;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.repository.CdrRepo;
import com.example.demo.repository.SubsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    CdrRepo cdrRepo;
    Random random = new Random();


    public List<CallEntity> generateCalls(int bot, int top) {
        List<CallEntity> cdrEntities = new ArrayList<>();
        List<SubscriberEntity> subscriberEntityList = subsRepo.findAll();
        List<SubscriberEntity> romashkaSubs=subsRepo.findByOperator_Id(1);
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
            long duration = random.nextLong(86400);
            Timestamp end = Timestamp.valueOf(startList.get(i).toLocalDateTime().plusSeconds(duration));
            CallEntity cdr;
            if (subscriberEntityList.get(s).getOperator().getId()==1) {
                int r = (random.nextInt(1, subscriberEntityList.size()) + s) % subscriberEntityList.size();
                cdr = new CallEntity(null, subscriberEntityList.get(s), subscriberEntityList.get(r), startList.get(i), end);
            }
            else{
                int r=random.nextInt(romashkaSubs.size());
                cdr = new CallEntity(null, subscriberEntityList.get(s), romashkaSubs.get(r), startList.get(i), end);
            }


            cdr = cdrRepo.saveAndFlush(cdr);
            cdrEntities.add(cdr);
        }

        return cdrEntities;


    }
}
