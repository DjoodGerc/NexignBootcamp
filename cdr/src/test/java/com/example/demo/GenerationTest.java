package com.example.demo;


import com.example.demo.dto.CdrDto;
import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.exception.DataAlreadyGeneratedException;
import com.example.demo.repository.CallRepo;
import com.example.demo.repository.SubsRepo;
import com.example.demo.services.GenerationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GenerationTest {
    @InjectMocks
    @Autowired
    GenerationService generationService;
    @Autowired
    SubsRepo subsRepo;
    @Autowired
    CallRepo callRepo;
    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    void generateSubsTest() throws DataAlreadyGeneratedException {
        callRepo.truncateTable();

        ArgumentCaptor<List<CdrDto>> dataCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);

        when(rabbitTemplate.convertSendAndReceive(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                dataCaptor.capture()
        ))
                .thenReturn(List.of());

        System.out.println(generationService.generateCalls(4000,5000));

        //все cdr отчеты
        List<List<CdrDto>> allSentData = dataCaptor.getAllValues();

        //проверяем что у нас звонящий и принимающий соответствуют флагам и что у нас отчет отправлен по одному абоненту
        //проверяем, что следующий звонок всегда позже предыдущего
        for (int i = 0; i < allSentData.size(); i++) {
            List<CdrDto> batch=allSentData.get(i);
            String ownerMsisdn;
            if (batch.get(0).getFlag().equals("01")){
                ownerMsisdn=batch.get(0).getInitiator();
            }else{
                ownerMsisdn=batch.get(0).getReceiver();
            }

            for (int j = 0; j < allSentData.get(i).size(); j++) {
                if (batch.get(i).getFlag().equals("01")){
                    assertEquals(ownerMsisdn,batch.get(i).getInitiator());
                }
                else{
                    assertEquals(ownerMsisdn,batch.get(i).getReceiver());
                }
                assertNotEquals(batch.get(i).getInitiator(),batch.get(i).getReceiver());
                if (i<allSentData.size()-1) {
                    assertTrue(batch.get(i).getEndDate().isBefore(batch.get(i + 1).getEndDate()));
                }
            }
        }

        //проверяем, что записалось в бд, те же вводные
        List<SubscriberEntity> subs=subsRepo.findAll();
        for (SubscriberEntity s: subs){
            List<CallEntity> calls=callRepo.findByInitiating_IdOrReceiving_Id(s.getId(),s.getId());
            calls.sort(Comparator.comparing(CallEntity::getStartCall));
            for (int i = 0; i < calls.size()-1; i++) {
                assertNotEquals(calls.get(i).getInitiating(),calls.get(i).getReceiving());
                assertTrue(calls.get(i).getEndCall().isBefore(calls.get(i+1).getStartCall()));
            }
        }

        assertThrows(DataAlreadyGeneratedException.class,()->generationService.generateCalls(1,2),"Data Already Generated");

    }




}
