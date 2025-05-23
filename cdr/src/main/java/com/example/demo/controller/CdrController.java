package com.example.demo.controller;

import com.example.demo.entity.CallEntity;
import com.example.demo.entity.SubscriberEntity;
import com.example.demo.exception.DataAlreadyGeneratedException;
import com.example.demo.repository.CallRepo;
import com.example.demo.repository.SubsRepo;
import com.example.demo.services.GenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * RestController
 * Может быть разделен на несколько, при необходимости.
 */
@RestController
public class CdrController {
    @Autowired
    CallRepo callRepo;
    @Autowired
    GenerationService generationService;
    @Autowired
    SubsRepo subsRepo;

    //для тестов
    @GetMapping(value = "/getSubs")
    public ResponseEntity<List<SubscriberEntity>> getSubs() {
        List<SubscriberEntity> subscriberEntityList = subsRepo.findAll();
        return new ResponseEntity<>(subscriberEntityList, HttpStatus.OK);
    }

    //для тестов
    @GetMapping(value = "/getSub/{msisdn}")
    public ResponseEntity<SubscriberEntity> getSubByMsisdn(@PathVariable(name = "msisdn") String msisdn) {
        SubscriberEntity subscriberEntity = subsRepo.findByMsisdn(msisdn).orElseThrow();
        return new ResponseEntity<>(subscriberEntity, HttpStatus.OK);
    }

    //для тестов
    @GetMapping(value = "/getAllCalls")
    public ResponseEntity<List<CallEntity>> getAllCalls() {
        List<CallEntity> callEntities = callRepo.findAll();
        return new ResponseEntity<>(callEntities, HttpStatus.OK);
    }

    //генерируем данные bot - нижнее значение; top - верхнее
    @PostMapping(value = "/generate")
    @ResponseBody
    public ResponseEntity<?> getGenerate(@RequestParam(name = "bot", defaultValue = "1", required = false) Optional<Integer> bot, @RequestParam(name = "top", required = false, defaultValue = "5000") Optional<Integer> top) throws DataAlreadyGeneratedException {
        int nCalls = generationService.generateCalls(bot.orElse(1), top.orElse(5000));
        return new ResponseEntity<>(String.format("%s calls generated successfully", nCalls), HttpStatus.OK);
    }

    @DeleteMapping(value = "/truncate")
    public ResponseEntity<?> truncate() {
        callRepo.truncateTable();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
