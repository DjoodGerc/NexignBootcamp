package brtApp;

import brtApp.Service.BalanceChangesService;
import brtApp.Service.SubscriberService;
import brtApp.dto.CdrDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.SubscriberEntity;
import brtApp.exception.NotRomashkaException;
import brtApp.exception.TarifficationException;
import brtApp.exception.TooEarlyForTarifficationException;
import brtApp.repository.SubscriberRepository;
import brtApp.client.HrsRest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class SubscriberServiceTest {
    @Mock
    @Autowired
    private HrsRest hrsRest;

    @Mock
    private BalanceChangesService balanceChangesService;

    @Autowired
    SubscriberRepository subscriberRepository;

    @InjectMocks
    @Autowired
    @Spy
    SubscriberService subscriberService;
    @Test
    public void findSubsByOwnerTest(){
        CdrDto notRomashkaCdrDto=new CdrDto("01","79999999999","711111111111", LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rNotToRCdr =new CdrDto("02","711111111111","79567890123", LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rToRCdr= new CdrDto("01","79123456789","79567890123",LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));

        assertThrows(EntityNotFoundException.class,()->subscriberService.findSubscriberByOwner(notRomashkaCdrDto),"Subscriber not found with MSISDN: 79999999999");

    }

    @Test
    public void validateSubsTest(){
        assertTrue(subscriberService.validateSubscriber("79123456789"));
        assertThrows(NotRomashkaException.class,()->subscriberService.validateSubscriber("79999999999"),"Not Romashka subscriber");
    }
    @Test
    public void changeBalanceCallTarifficationTest(){
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79123456789").get();
        HrsRetrieveDto hrsRetrieveDto=new HrsRetrieveDto(52L ,52.52);
        Long oldTariffBalance=subscriber.getTariffBalance();
        Double oldBalance=subscriber.getBalance();
        SubscriberEntity newSub=subscriberService.changeBalanceCallTariffication(subscriber,hrsRetrieveDto);
        assertEquals(52L,newSub.getTariffBalance()-oldTariffBalance);
        assertTrue(52.52+0.001>newSub.getBalance()-oldBalance&&52.52-0.001<newSub.getBalance()-oldBalance);

    }

    @Test
    void monthTarifficationTooEarlyExcTest() {
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79890123456").get();
        HrsRetrieveDto mockHrsDto = new HrsRetrieveDto();
        mockHrsDto.setBalanceChange(-50.2);
        mockHrsDto.setTariffBalanceChange(20L);

        subscriber.setLastMonthTarifficationDate(LocalDateTime.now().minusHours(1));

        assertThrows(TooEarlyForTarifficationException.class,()->subscriberService.monthTariffication(subscriber, LocalDateTime.now()));

        verify(hrsRest, never()).getMonthTariffFeeAndMinutes(subscriber.getTariffId());

    }


    @Test
    void monthTarifficationTest() {
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79890123456").get();
        HrsRetrieveDto mockHrsDto = new HrsRetrieveDto();
        mockHrsDto.setBalanceChange(-50.2);
        mockHrsDto.setTariffBalanceChange(20L);
        Double oldBalance=subscriber.getBalance();
        subscriber.setLastMonthTarifficationDate(null);
        when(hrsRest.getMonthTariffFeeAndMinutes(anyLong())).thenReturn(mockHrsDto);

        SubscriberEntity result = subscriberService.monthTariffication(subscriber, LocalDateTime.now());

        assertNotNull(result.getLastMonthTarifficationDate());
        verify(hrsRest, times(1)).getMonthTariffFeeAndMinutes(subscriber.getTariffId());
        System.out.println(oldBalance);
        System.out.println(result.getBalance());
        System.out.println(result.getBalance()+50.2);
        assertTrue(-50.2+0.001>result.getBalance()-oldBalance&&-50.2-0.001<result.getBalance()-oldBalance);
        assertEquals(20L,result.getTariffBalance());
    }
    @Test
    void monthTariffication2MonthTest() {
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79890123456").get();
        HrsRetrieveDto mockHrsDto = new HrsRetrieveDto();
        mockHrsDto.setBalanceChange(-50.2);
        mockHrsDto.setTariffBalanceChange(20L);
        Double oldBalance=subscriber.getBalance();
        subscriber.setLastMonthTarifficationDate(LocalDateTime.now().minusDays(70));
        when(hrsRest.getMonthTariffFeeAndMinutes(anyLong())).thenReturn(mockHrsDto);

        SubscriberEntity result = subscriberService.monthTariffication(subscriber, LocalDateTime.now());

        assertNotNull(result.getLastMonthTarifficationDate());
        verify(hrsRest, times(1)).getMonthTariffFeeAndMinutes(subscriber.getTariffId());
        System.out.println(oldBalance);
        System.out.println(result.getBalance());
        System.out.println(result.getBalance()+50.2);
        assertTrue(-(50.2)+0.001>result.getBalance()-oldBalance&&-(50.2)-0.001<result.getBalance()-oldBalance);
        assertEquals(20L,result.getTariffBalance());
    }
    @Test
    void monthTarifficationClientExceptionTest() {
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79890123456").get();
        HrsRetrieveDto mockHrsDto = new HrsRetrieveDto();
        mockHrsDto.setBalanceChange(-50.15);
        mockHrsDto.setTariffBalanceChange(20L);
        subscriber.setLastMonthTarifficationDate(null);
        when(hrsRest.getMonthTariffFeeAndMinutes(anyLong())).thenThrow(new TarifficationException(HttpStatus.BAD_REQUEST,"reason"));

        assertThrows(TarifficationException.class,()->subscriberService.monthTariffication(subscriber, LocalDateTime.now()));
    }


    @Test
    void checkMonthTarifficationDateTest(){
        SubscriberEntity subscriber=subscriberRepository.findByMsisdn("79890123456").get();
    }


}
