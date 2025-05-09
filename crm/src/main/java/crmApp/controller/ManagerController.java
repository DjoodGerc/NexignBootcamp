package crmApp.controller;

import crmApp.dto.*;
import crmApp.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @PostMapping(value = "/manager/subscriber/add")
    public ResponseEntity<BrtRetrieveSubsData> addSubscriber(@RequestBody SubscriberCrmDto subscriberDataDto) throws Exception {
        BrtRetrieveSubsData result=managerService.hrsAddSubscriber(subscriberDataDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/manager/subscriber/{msisdn}/fullinfo")
    public ResponseEntity<BrtRetrieveSubsData> getSubsFullInfo(@PathVariable(name ="msisdn") String msisdn) throws Exception {
        BrtRetrieveSubsData result=managerService.hrsGetSubsFullInfo(msisdn);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PatchMapping(value = "/manager/subscriber/{msisdn}/delete")
    public ResponseEntity<DeleteStatusDto> updateSubscriber(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        DeleteStatusDto deleteStatusDto=managerService.hrsDeleteSubscriber(msisdn);
        return new ResponseEntity<>(deleteStatusDto, HttpStatus.OK);
    }


    @GetMapping(value = "/manager/subscriber/{msisdn}/gettariff")
    public ResponseEntity<SubsTariffDto> getSubsTariff(@PathVariable(name ="msisdn") String msisdn) throws Exception {
        SubsTariffDto result=managerService.getSubsTariff(msisdn);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/manager/subscriber/changetariff")
    public ResponseEntity<SubsTariffDto> changeSubsTariff(@RequestBody ChangeTariffDto changeTariffDto) throws Exception {
        SubsTariffDto result=managerService.changeSubsTariff(changeTariffDto.getMsisdn(),changeTariffDto.getNewTariffId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }






}
