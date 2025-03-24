package com.example.demo;

import com.example.demo.entity.CdrEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.repository.SubsRepo;
import com.example.demo.services.GenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GenerationTest {
    @Autowired
    GenerationService generationService;
    @Autowired
    SubsRepo subsRepo;

    @Test
    void generateSubsTest() {
        List<SubscriberEntity> subscriberEntityList = generationService.generateSubs(3);
        for (SubscriberEntity s : subscriberEntityList) {
            String sNumber = s.getNumber();
            String digitsOnly = sNumber.replaceAll("[^0-9]", "");
            assertEquals(11, digitsOnly.length());
            assertEquals('7', sNumber.charAt(0));
        }
    }

    @Test
    void generationCdrTest() {
        List<SubscriberEntity> subscriberEntityList = generationService.generateSubs(3);
        List<CdrEntity> cdrEntities = generationService.generateCdr();
        for (CdrEntity cdr : cdrEntities) {
            assertNotEquals(cdr.getInitiating(), cdr.getReceiving());
            assertTrue(cdr.getStartCall().before(cdr.getEndCall()));
        }
    }

}
