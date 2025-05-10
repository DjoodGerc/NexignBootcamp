package hrsApp.controller;

import hrsApp.dto.HrsCallDto;
import hrsApp.dto.HrsFeeDto;
import hrsApp.entity.TariffEntity;
import hrsApp.exception.MonthTarifficationIsNotAllowedForEventTariffException;
import hrsApp.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HrsController {
    @Autowired
    TariffService tariffService;

    @PostMapping(value = "/tarifficateCall")
    public ResponseEntity<HrsFeeDto> tarifficateCall(@RequestBody HrsCallDto hrsCallDto)  {
        HrsFeeDto hrsFeeDto=tariffService.tarifficateCall(hrsCallDto);
        return new ResponseEntity<>(hrsFeeDto, HttpStatus.OK);
    }
    @GetMapping(value = "/monthTariffication/{id}")
    public ResponseEntity<HrsFeeDto> processCdrList(@PathVariable Long id) throws MonthTarifficationIsNotAllowedForEventTariffException {
        HrsFeeDto hrsFeeDto=tariffService.monthTariffication(id);
        return new ResponseEntity<>(hrsFeeDto, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllTariffs")
    public ResponseEntity<List<TariffEntity>> getAllTariffs(){
        List<TariffEntity> tariffEntities=tariffService.getAllTariffs();
        return new ResponseEntity<>(tariffEntities, HttpStatus.OK);
    }

    @GetMapping(value = "/getTariffById/{id}")
    public ResponseEntity<TariffEntity> getTariffById(@PathVariable(name="id") long id){
        TariffEntity tariff=tariffService.getTariff(id);
        return new ResponseEntity<>(tariff, HttpStatus.OK);
    }




}
