package com.example.demo.services;

import com.example.demo.entity.CdrEntity;
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

@Service
public class GenerationService {
    @Autowired
    SubsRepo subsRepo;

    @Autowired
    CdrRepo cdrRepo;
    Random random = new Random();

    public List<SubscriberEntity> generateSubs(int nSubs){

        List<SubscriberEntity> subscriberEntityList=new ArrayList<>();
        for (int i = 0; i < nSubs; i++) {
            while (true) {
                StringBuilder phoneNumber = new StringBuilder();
                phoneNumber.append(7);
                for (int j = 0; j < 10; j++) {
                    phoneNumber.append(random.nextInt(10));
                }
                if (subsRepo.findByNumber(phoneNumber.toString()).isEmpty()){

                    subscriberEntityList.add(subsRepo.saveAndFlush(new SubscriberEntity(null,phoneNumber.toString())));
                    break;
                }
            }

        }
        return subscriberEntityList;
    }

    public List<CdrEntity> generateCdr(){
        List<CdrEntity> cdrEntities=new ArrayList<>();
        List<SubscriberEntity> subscriberEntityList=subsRepo.findAll();
        long startTimestamp=LocalDateTime.now().minusYears(1).minusDays(1).toEpochSecond(ZoneOffset.UTC);
        long endTimestamp=LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC);



        int nCalls=random.nextInt(1,10);
        List<Timestamp> startList =new ArrayList<>();
        List<Long> durationList=new ArrayList<>();
        for (int j = 0; j < nCalls; j++) {
            long randomTimestamp = startTimestamp + (long) (random.nextDouble() * (endTimestamp - startTimestamp));
            startList.add(Timestamp.from(Instant.ofEpochSecond(randomTimestamp)));
        }

        Collections.sort(startList);
        for (int i = 0; i < startList.size(); i++) {
            int s=random.nextInt(0,subscriberEntityList.size());
            int r=(random.nextInt(0,subscriberEntityList.size())+s)%subscriberEntityList.size();
            long duration=random.nextLong(86400);
            Timestamp end=Timestamp.valueOf(startList.get(i).toLocalDateTime().plusSeconds(duration));
            CdrEntity cdr=new CdrEntity(null,subscriberEntityList.get(s),subscriberEntityList.get(r),startList.get(i),end);
            cdr=cdrRepo.saveAndFlush(cdr);
            cdrEntities.add(cdr);
        }

        return cdrEntities;



    }
//    public void generateCdr(int nCalls,int n_subs){
//
//    }
}
