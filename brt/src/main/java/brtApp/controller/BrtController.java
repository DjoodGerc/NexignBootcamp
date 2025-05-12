package brtApp.controller;

import brtApp.Service.CallService;
import brtApp.Service.SubscriberService;
import brtApp.dto.*;
import brtApp.entity.SubscriberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BrtController {
    @Autowired
    CallService callService;
    @Autowired
    SubscriberService subscriberService;


    //служебная api для тестирования
    @PostMapping(value = "/processCdrList")
    public ResponseEntity<List<HrsRetrieveDto>> processCdrList(@RequestBody List<CdrDto> cdrDtoList) throws Exception {
        List<HrsRetrieveDto> changes = callService.processCdrList(cdrDtoList);
        return new ResponseEntity<>(changes, HttpStatus.OK);
    }

    //все апи нужны для взаимодействия с crm (по хорошему повесить бы сюда security,
    // чтобы можно было стучаться только с токеном,
    //но имеем, что имеем)
    @PostMapping(value = "/addSubscriber")
    public ResponseEntity<SubscriberEntity> addSubs(@RequestBody SubscriberCrmDto subsCrm) throws Exception {
        SubscriberEntity subscriber = subscriberService.saveNewSub(subsCrm);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }


    @GetMapping(value = "/getSubscriberFullInfo/{msisdn}")
    public ResponseEntity<SubscriberEntity> getSubInfo(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        SubscriberEntity subscriber = subscriberService.getSubscriber(msisdn);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }


    @PatchMapping(value = "/updateSubscriber")
    public ResponseEntity<SubscriberEntity> updateSub(@RequestBody SubscriberCrmDto subscriberCrmDto) throws Exception {
        SubscriberEntity subscriber = subscriberService.updateSub(subscriberCrmDto);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteSubscriber/{msisdn}")
    public ResponseEntity<DeleteStatusDto> deleteSub(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        DeleteStatusDto res = subscriberService.deleteSub(msisdn);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping(value = "/changeSubsTariff/{msisdn}/{newTariffId}")
    public ResponseEntity<SubscriberEntity> updateSub(@PathVariable(name = "msisdn") String msisdn, @PathVariable(name = "newTariffId") long newTariffId) throws Exception {
        SubscriberEntity subscriber = subscriberService.changeTariff(msisdn, newTariffId);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }

    @PutMapping(value = "/changeSubsBalance/{msisdn}")
    public ResponseEntity<SubscriberEntity> changeSubBalance(@PathVariable(name = "msisdn") String msisdn, @RequestBody ChangeBalanceDto changeBalanceDto) {
        SubscriberEntity subscriber = subscriberService.changeSubBalance(msisdn, changeBalanceDto);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }


}
