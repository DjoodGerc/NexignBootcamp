//package com.example.demo;
//
//import com.example.demo.entity.CallEntity;
//import com.example.demo.entity.SubscriberEntity;
//import com.example.demo.repository.CallRepo;
//import com.example.demo.repository.SubsRepo;
//import com.example.demo.services.CdrService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@SpringBootTest
//public class CdrServiceTest {
//    @Autowired
//    CallRepo callRepo;
//    @Autowired
//    SubsRepo subsRepo;
//    @Autowired
//    CdrService cdrService;
//
////    @Test
////    void cdrReport() throws IOException {
////        SubscriberEntity s1 = subsRepo.saveAndFlush(new SubscriberEntity(null, "71111111111"));
////        SubscriberEntity s2 = subsRepo.saveAndFlush(new SubscriberEntity(null, "72222222222"));
////
////        callRepo.saveAndFlush(new CallEntity(null, s1, s2, Timestamp.valueOf(LocalDateTime.now().minusMinutes(3)), Timestamp.valueOf(LocalDateTime.now())));
////        String fileName = cdrService.cdrReport(s1.getId(), s1.getMsisdn(), LocalDateTime.now().minusDays(1), LocalDateTime.now().plusMinutes(5));
////        assertEquals(fileName.split("_")[0], s1.getMsisdn());
////        //не успеваю проверить, что записалось в csv
////    }
//}
