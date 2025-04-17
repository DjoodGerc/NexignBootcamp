package brtApp.controller;

import brtApp.Service.CallService;
import brtApp.dto.CdrDto;
import brtApp.dto.HrsRetrieveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BrtController {
    @Autowired
    CallService callService;

    @PostMapping(value = "/processCdrList")
    public ResponseEntity<List<HrsRetrieveDto>> processCdrList(@RequestBody List<CdrDto> cdrDtoList) {
        List<HrsRetrieveDto> changes=callService.processCdrList(cdrDtoList);
        return new ResponseEntity<>(changes, HttpStatus.OK);
    }
    @GetMapping(name = "/get",value = "/get")
    public ResponseEntity<?> get(){
        return new ResponseEntity<>("все ок", HttpStatus.OK);
    }
    @GetMapping(value = "/")
    public ResponseEntity<String> m(){
        return new ResponseEntity<>("все ок", HttpStatus.OK);
    }
}
