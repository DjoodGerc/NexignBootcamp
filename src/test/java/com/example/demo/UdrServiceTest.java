package com.example.demo;

import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CdrEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.services.GenerationService;
import com.example.demo.services.UdrService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UdrServiceTest {
    @Autowired
    GenerationService generationService;
    @Autowired
    UdrService udrService;

    @Test
    void udrForALl() {
        List<SubscriberEntity> subscriberEntityList = generationService.generateSubs(3);
        List<String> numbers = new ArrayList<>();
        for (SubscriberEntity s : subscriberEntityList) {
            numbers.add(s.getNumber());
        }
        List<CdrEntity> cdrEntityList = generationService.generateCdr();
        List<UdrDto> udrDtos = udrService.UdrReportForAll(cdrEntityList);
        for (UdrDto udr : udrDtos) {
            assertTrue(numbers.contains(udr.getMsisdn()));
        }
        assertEquals(udrDtos.size(), numbers.size());
    }
}
