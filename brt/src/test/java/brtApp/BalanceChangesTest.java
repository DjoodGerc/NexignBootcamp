package brtApp;

import brtApp.Service.BalanceChangesService;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.BalanceChangeEnum;
import brtApp.entity.BalanceChangesEntity;
import brtApp.entity.SubscriberEntity;
import brtApp.rabbit.Listener;
import brtApp.repository.BalanceChangesRepository;
import brtApp.repository.SubscriberRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class BalanceChangesTest {
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    BalanceChangesService balanceChangesService;
    @Autowired
    BalanceChangesRepository balanceChangesRepository;
    @Mock
    Listener listener;

    @Test
    public void saveChangeEntityTest(){
        HrsRetrieveDto hrsRetrieveDto=new HrsRetrieveDto(52L,12.12);
        SubscriberEntity subscriber=subscriberRepository.findById(1L).get();

        BalanceChangesEntity savedEntityInc =balanceChangesService.saveChangeEntity(hrsRetrieveDto.getBalanceChange(),subscriber, LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))));
        assertEquals(savedEntityInc.getChangeType().getId(), BalanceChangeEnum.INCREASE.getId());
        hrsRetrieveDto=new HrsRetrieveDto(0L,-123.12);
        BalanceChangesEntity savedEntityDec=balanceChangesService.saveChangeEntity(hrsRetrieveDto.getBalanceChange(),subscriber,LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))));
        List<BalanceChangesEntity> balanceChangesEntityList=balanceChangesRepository.findAll();
        assertTrue(balanceChangesEntityList.contains(savedEntityInc));
        assertTrue(balanceChangesEntityList.contains(savedEntityDec));

    }
}

