package com.example.demo.controller;

import com.example.demo.dto.ReportInputData;
import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CdrEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.repository.CdrRepo;
import com.example.demo.repository.SubsRepo;
import com.example.demo.services.CdrService;
import com.example.demo.services.GenerationService;
import com.example.demo.services.UdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * RestController
 * Может быть разделен на несколько, при необходимости.
 */
@RestController
public class MainController {
    @Autowired
    CdrRepo cdrRepo;
    @Autowired
    GenerationService generationService;
    @Autowired
    UdrService udrService;
    @Autowired
    SubsRepo subsRepo;
    @Autowired
    CdrService cdrService;


    @GetMapping(value = "/getSubs")
    public ResponseEntity<List<SubscriberEntity>> getSubs() {
        List<SubscriberEntity> subscriberEntityList = subsRepo.findAll();
        return new ResponseEntity<>(subscriberEntityList, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllCdr")
    public ResponseEntity<List<CdrEntity>> getAllCdr() {
        List<CdrEntity> cdrEntities = cdrRepo.findAll();
        return new ResponseEntity<>(cdrEntities, HttpStatus.OK);
    }

    @PostMapping(value = "/generate")
    public ResponseEntity<Object> getGenerate() {
        List<CdrEntity> cdrEntities = cdrRepo.findAll();
        if (!cdrEntities.isEmpty()) {
            return new ResponseEntity<>("Data already generated", HttpStatus.OK);
        }
        cdrEntities = generationService.generateCdr(1,5000);
        return new ResponseEntity<>(cdrEntities, HttpStatus.OK);
    }

    @PostMapping(value = "/generate/{bot}/{top}")
    public ResponseEntity<Object> getGenerate(@PathVariable(name = "bot") int bot,@PathVariable(name = "top") int top) {
        List<CdrEntity> cdrEntities = cdrRepo.findAll();
        if (!cdrEntities.isEmpty()) {
            return new ResponseEntity<>("Data already generated", HttpStatus.OK);
        }
        cdrEntities = generationService.generateCdr(bot,top);
        return new ResponseEntity<>(cdrEntities, HttpStatus.OK);
    }

    @GetMapping(value = "/getUdr")
    public ResponseEntity<List<UdrDto>> getUdr() {
        List<UdrDto> udrDtos = udrService.UdrReportForAll(cdrRepo.findAll());
        return new ResponseEntity<>(udrDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/getUdr/byYear/{year}/byMonth/{month}")
    public ResponseEntity<List<UdrDto>> getUdr(@PathVariable(name = "year") int year, @PathVariable(name = "month") int month) {

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0, 0));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0, 0).plusMonths(1).minusSeconds(1));
        List<UdrDto> udrDtos = udrService.UdrReportForAll(cdrRepo.findByStartCallBetween(start, end));
        return new ResponseEntity<>(udrDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/getUdr/byNumber/{number}")
    public ResponseEntity<UdrDto> getUdr(@PathVariable(name = "number") String number) {
        SubscriberEntity subscriberEntity;
        try {
            subscriberEntity = subsRepo.findByNumber(number).orElseThrow();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Long subsId = subscriberEntity.getId();
        UdrDto udrDto = udrService.UdrReport(cdrRepo.findByInitiating_IdOrReceiving_Id(subsId, subsId), number);
        return new ResponseEntity<>(udrDto, HttpStatus.OK);
    }

    @GetMapping(value = "/getUdr/byNumber/{number}/byYear/{year}/byMonth/{month}")
    public ResponseEntity<UdrDto> getUdr(@PathVariable(name = "number") String number, @PathVariable(name = "year") int year, @PathVariable(name = "month") int month) {
        SubscriberEntity subscriberEntity;
        try {
            subscriberEntity = subsRepo.findByNumber(number).orElseThrow();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Long subsId = subscriberEntity.getId();
        Timestamp start = Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0, 0));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0, 0).plusMonths(1).minusSeconds(1));
        UdrDto udrDto = udrService.UdrReport(cdrRepo.findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(subsId, start, end, subsId, start, end), number);
        return new ResponseEntity<>(udrDto, HttpStatus.OK);
    }

    @PostMapping(value = "/createCdrReport")
    public ResponseEntity<String> getUdr(@RequestBody ReportInputData reportInputData) {
        StringBuilder resStringBuilder = new StringBuilder();
        long uid;
        try {
            SubscriberEntity subscriberEntity = subsRepo.findByNumber(reportInputData.getNumber()).orElseThrow();
            uid = subscriberEntity.getId();
        } catch (Exception e) {
            resStringBuilder.append("Subscriber not found_");
            resStringBuilder.append(UUID.randomUUID());
            return new ResponseEntity<>(resStringBuilder.toString(), HttpStatus.NOT_FOUND);

        }
        if (reportInputData.getStartDate().isAfter(reportInputData.getEndDate())) {
            resStringBuilder.append("Invalid dates_");
            resStringBuilder.append(UUID.randomUUID());
            return new ResponseEntity<>(resStringBuilder.toString(), HttpStatus.BAD_REQUEST);


        }
        String res = null;
        try {
            res = cdrService.cdrReport(uid, reportInputData.getNumber(), reportInputData.getStartDate(), reportInputData.getEndDate());
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage() + "_" + UUID.randomUUID(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
