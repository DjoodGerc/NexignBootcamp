package com.example.demo.controller;

import com.example.demo.entity.CdrEntity;
import com.example.demo.repository.CdrRepo;
import com.example.demo.services.GenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    CdrRepo cdrRepo;
    @Autowired
    GenerationService generationService;

    @GetMapping(value = "/getAllCdr")
    public ResponseEntity<List<CdrEntity>> getAllCdr(){
        List<CdrEntity> cdrEntities=cdrRepo.findAll();
        return new ResponseEntity<>(cdrEntities, HttpStatus.OK);
    }

    @PostMapping(value = "/generate")
    public ResponseEntity<List<CdrEntity>> getGenerate(){
        generationService.generateSubs(10);
        List<CdrEntity> cdrEntities=generationService.generateCdr();
        return new ResponseEntity<>(cdrEntities, HttpStatus.OK);
    }

}
