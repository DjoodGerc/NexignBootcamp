package crmApp.controller;

import crmApp.dto.BrtRetrieveSubsData;
import crmApp.dto.ChangeBalanceDto;
import crmApp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriberController {
    @Autowired
    ClientService clientService;

    @GetMapping(value = "/subscriber/{msisdn}/getbalance")
    public ResponseEntity<BrtRetrieveSubsData> getSubsBalance(@PathVariable(name ="msisdn") String msisdn,Authentication authentication) {
        BrtRetrieveSubsData result = null;
        if (clientService.CheckAuthority(authentication,msisdn)){
            result= clientService.hrsGetSubsFullInfo(msisdn);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/subscriber/{msisdn}/changebalance")
    public ResponseEntity<BrtRetrieveSubsData> changeSubBalance(@PathVariable(name ="msisdn") String msisdn, @RequestBody ChangeBalanceDto changeBalanceDto, Authentication authentication)  {
        BrtRetrieveSubsData result = null;
        if (clientService.CheckAuthority(authentication,msisdn)){
             result=clientService.changeBalance(msisdn,changeBalanceDto);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
