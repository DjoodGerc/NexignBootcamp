package hrsApp;

import hrsApp.dto.HrsCallDto;
import hrsApp.dto.HrsFeeDto;
import hrsApp.exception.MonthTarifficationIsNotAllowedForEventTariffException;
import hrsApp.service.TariffService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class TariffServiceTest {
    @Autowired
    TariffService tariffService;

    @Test
    public void monthTarifficationPositive() throws MonthTarifficationIsNotAllowedForEventTariffException {
        HrsFeeDto hrsFeeDto = tariffService.monthTariffication(12L);
        assertEquals(-100.0, hrsFeeDto.getBalanceChange());
        assertEquals(50, hrsFeeDto.getTariffBalanceChange());

        assertThrows(MonthTarifficationIsNotAllowedForEventTariffException.class, ()->tariffService.monthTariffication(11L));
        assertThrows(EntityNotFoundException.class,()->tariffService.monthTariffication(13L));
    }

    @Test
    public void tarifficateCallClassicTariff(){
        HrsCallDto hrsCallDto=new HrsCallDto(10,1L,true,11L,0,100.0);

        HrsFeeDto hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-10*1.5,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(0,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,1L,false,11L,0,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-10*2.5,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(0,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,2L,true,11L,0,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-0.0,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(0,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,2L,false,11L,0,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-0.0,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(0,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,2L,false,13L,0,100.0);
        HrsCallDto finalHrsCallDto = hrsCallDto;
        assertThrows(EntityNotFoundException.class,()->tariffService.tarifficateCall(finalHrsCallDto));


        hrsCallDto=new HrsCallDto(10,3L,false,12L,0,100.0);
        HrsCallDto fina2lHrsCallDto = hrsCallDto;
        assertThrows(EntityNotFoundException.class,()->tariffService.tarifficateCall(fina2lHrsCallDto));


    }


    @Test
    public void tarifficateCallMonthTariff(){
        HrsCallDto hrsCallDto=new HrsCallDto(10,1L,true,12L,5,100.0);
        HrsFeeDto hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-5*1.5,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(-5,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,1L,false,12L,12,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(   0.0,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(-10,hrsFeeDto.getTariffBalanceChange(),0.001);

        hrsCallDto=new HrsCallDto(10,1L,false,12L,5,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-5*2.5,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(-5,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,2L,true,12L,5,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-0.0,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(-5,hrsFeeDto.getTariffBalanceChange());

        hrsCallDto=new HrsCallDto(10,2L,false,12L,5,100.0);
        hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        assertEquals(-0.0,hrsFeeDto.getBalanceChange(),0.001);
        assertEquals(-5,hrsFeeDto.getTariffBalanceChange());
    }



}
