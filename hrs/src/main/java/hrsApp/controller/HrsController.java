package hrsApp.controller;

import hrsApp.dto.HrsCallDto;
import hrsApp.dto.HrsFeeDto;
import hrsApp.exception.MonthTarifficationIsNotAllowedForEventTariffException;
import hrsApp.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HrsController {
    @Autowired
    TariffService tariffService;

    @GetMapping(value = "/tarifficateCall")
    public ResponseEntity<HrsFeeDto> tarifficateCall(@RequestBody HrsCallDto hrsCallDto)  {
        HrsFeeDto hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        return new ResponseEntity<>(hrsFeeDto, HttpStatus.OK);
    }
    @GetMapping(value = "/monthTariffication/{id}")
    public ResponseEntity<HrsFeeDto> processCdrList(@PathVariable Long id) throws MonthTarifficationIsNotAllowedForEventTariffException {
        HrsFeeDto hrsFeeDto=tariffService.monthTariffication(id);
        return new ResponseEntity<>(hrsFeeDto, HttpStatus.OK);
    }
}
