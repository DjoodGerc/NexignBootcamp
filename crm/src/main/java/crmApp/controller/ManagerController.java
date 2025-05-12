package crmApp.controller;

import crmApp.dto.*;
import crmApp.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagerController {

    @Autowired
    ClientService clientService;

    //описаны в сваггере
    //сохраняем новичка
    @PostMapping(value = "/manager/subscriber/add")
    public ResponseEntity<BrtRetrieveSubsData> addSubscriber(@RequestBody SubscriberCrmDto subscriberDataDto) throws Exception {
        BrtRetrieveSubsData result = clientService.hrsAddSubscriber(subscriberDataDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //обновляем старичка
    @PatchMapping(value = "/manager/subscriber/update")
    public ResponseEntity<BrtRetrieveSubsData> updateSubscriber(@RequestBody SubscriberCrmDto subscriberDataDto) throws Exception {
        BrtRetrieveSubsData result = clientService.hrsUpdateSubscriber(subscriberDataDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //получаем инфу
    @GetMapping(value = "/manager/subscriber/{msisdn}/fullinfo")
    public ResponseEntity<BrtRetrieveSubsData> getSubsFullInfo(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        BrtRetrieveSubsData result = clientService.hrsGetSubsFullInfo(msisdn);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    //удаляем
    @DeleteMapping(value = "/manager/subscriber/{msisdn}/delete")
    public ResponseEntity<DeleteStatusDto> deleteSubscriber(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        DeleteStatusDto deleteStatusDto = clientService.hrsDeleteSubscriber(msisdn);
        return new ResponseEntity<>(deleteStatusDto, HttpStatus.OK);
    }

    //получаем данные по тарифу
    @GetMapping(value = "/manager/subscriber/{msisdn}/gettariff")
    public ResponseEntity<SubsTariffDto> getSubsTariff(@PathVariable(name = "msisdn") String msisdn) throws Exception {
        SubsTariffDto result = clientService.getSubsTariff(msisdn);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //меняем тариф
    @PutMapping(value = "/manager/subscriber/changetariff")
    public ResponseEntity<SubsTariffDto> changeSubsTariff(@RequestBody ChangeTariffDto changeTariffDto) throws Exception {
        SubsTariffDto result = clientService.changeSubsTariff(changeTariffDto.getMsisdn(), changeTariffDto.getNewTariffId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
