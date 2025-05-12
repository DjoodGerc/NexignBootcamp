package brtApp;

import brtApp.Service.CdrService;
import brtApp.dto.CdrDto;
import brtApp.entity.CallEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CdrServiceTest {
    @Autowired
    CdrService cdrService;

    @Test
    public void cdrServiceTest() throws JSONException, JsonProcessingException {
        JacksonJsonParser jsonParser=new JacksonJsonParser();
        ObjectMapper mapper =new ObjectMapper().registerModule(new JavaTimeModule());
        String s="[{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79012345678\",\"startDate\":\"2024-04-17T14:26:13\",\"endDate\":\"2024-04-17T22:14:17\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79789012345\",\"startDate\":\"2024-04-18T18:46:25\",\"endDate\":\"2024-04-19T00:00:00\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79789012345\",\"startDate\":\"2024-04-19T00:00:00\",\"endDate\":\"2024-04-19T00:25:30\"},{\"flag\":\"02\",\"initiator\":\"79567890123\",\"receiver\":\"79890123456\",\"startDate\":\"2024-04-20T09:19:51\",\"endDate\":\"2024-04-20T14:40:28\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79901234567\",\"startDate\":\"2024-04-20T22:59:33\",\"endDate\":\"2024-04-21T00:00:00\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79901234567\",\"startDate\":\"2024-04-21T00:00:00\",\"endDate\":\"2024-04-21T10:33:04\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79234567890\",\"startDate\":\"2024-04-21T17:46:17\",\"endDate\":\"2024-04-22T00:00:00\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79234567890\",\"startDate\":\"2024-04-22T00:00:00\",\"endDate\":\"2024-04-22T03:52:41\"},{\"flag\":\"01\",\"initiator\":\"79890123456\",\"receiver\":\"79678901234\",\"startDate\":\"2024-04-22T16:56:08\",\"endDate\":\"2024-04-22T21:50:47\"},{\"flag\":\"02\",\"initiator\":\"79567890123\",\"receiver\":\"79890123456\",\"startDate\":\"2024-04-23T04:34:40\",\"endDate\":\"2024-04-23T16:28:26\"}]\n";
        CdrDto notRomashkaCdrDto=new CdrDto("01","79999999999","711111111111", LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rNotToRCdr =new CdrDto("02","711111111111","79567890123",LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));
        CdrDto rToRCdr= new CdrDto("01","79123456789","79567890123",LocalDateTime.now().minusHours(1),LocalDateTime.now().minusMinutes(1));

        CallEntity rNotToRCall =cdrService.callEntityFromCdr(rNotToRCdr);
        assertEquals(rNotToRCall.getOpponentMsisdn(),"711111111111");
        assertFalse(rNotToRCall.getIsRomashkaCall());
        assertEquals(2,rNotToRCall.getCallType().getId());

        CallEntity rToRCall=cdrService.callEntityFromCdr(rToRCdr);
        assertEquals(rToRCall.getOpponentMsisdn(),"79567890123");
        assertTrue(rToRCall.getIsRomashkaCall());
        assertEquals(1,rToRCall.getCallType().getId());

        assertThrows(EntityNotFoundException.class,()->cdrService.callEntityFromCdr(notRomashkaCdrDto),"Subscriber not found with MSISDN: " + "79999999999");

    }
}
