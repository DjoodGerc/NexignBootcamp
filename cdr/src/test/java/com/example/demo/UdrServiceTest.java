//package com.example.demo;
//
//import com.example.demo.brtApp.dto.UdrDto;
//import com.example.demo.brtApp.entity.CallEntity;
//import com.example.demo.brtApp.entity.SubscriberEntity;
//import com.example.demo.services.GenerationService;
//import com.example.demo.services.UdrService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class UdrServiceTest {
//    @Autowired
//    GenerationService generationService;
//    @Autowired
//    UdrService udrService;
//
////    @Test
////    void udrForALl() {
////        List<SubscriberEntity> subscriberEntityList = generationService.generateSubs(3);
////        List<String> numbers = new ArrayList<>();
////        for (SubscriberEntity s : subscriberEntityList) {
////            numbers.add(s.getMsisdn());
////        }
////        List<CallEntity> callEntityList = generationService.generateCdr();
////        List<UdrDto> udrDtos = udrService.UdrReportForAll(callEntityList);
////        for (UdrDto udr : udrDtos) {
////            assertTrue(numbers.contains(udr.getMsisdn()));
////        }
////        assertEquals(udrDtos.size(), numbers.size());
////    }
//}
