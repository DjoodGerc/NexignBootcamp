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
    public ResponseEntity<List<HrsRetrieveDto>> processCdrList(@RequestBody List<CdrDto> cdrDtoList) throws Exception {
        List<HrsRetrieveDto> changes = callService.processCdrList(cdrDtoList);
        return new ResponseEntity<>(changes, HttpStatus.OK);
    }

}
