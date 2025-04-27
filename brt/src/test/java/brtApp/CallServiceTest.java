package brtApp;

import brtApp.Service.CallService;
import brtApp.dto.CdrDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class CallServiceTest {
    @Autowired
    CallService callService;
    @Test
    public void processSingleCdrRomashkaToRomashkaTest(){
        CdrDto dto=new CdrDto();
        CdrDto notRomashkaCdrDto=new CdrDto("01","79999999999","711111111111", LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rNotToRCdr =new CdrDto("02","711111111111","79567890123",LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rToRCdr= new CdrDto("01","79123456789","79567890123",LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));



    }
}
