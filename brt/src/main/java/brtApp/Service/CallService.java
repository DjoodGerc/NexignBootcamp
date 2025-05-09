package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.CallEntity;
import brtApp.entity.SubscriberEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import brtApp.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import brtApp.repository.CallRepository;
import brtApp.client.HrsRest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CallService {
    @Autowired
    SubscriberService subscriberService;
    @Autowired
    MyMapper mapper;
    @Autowired
    HrsRest hrsRest;
    @Autowired
    CallRepository callRepository;
    @Autowired
    BalanceChangesService balanceChangesService;
    @Autowired
    CdrService cdrService;

    public List<HrsRetrieveDto> processCdrList(List<CdrDto> cdrDtoList) {
        String ownerMsisdn = "";

        for (int i = 0; i < cdrDtoList.size(); i++) {
            if (cdrDtoList.get(i).getFlag().equals("01")||cdrDtoList.get(i).getFlag().equals("02")){
                ownerMsisdn=cdrDtoList.get(i).getOwner();
            }
        }
        if (ownerMsisdn.isEmpty()){

            log.error("Not found valid flag in any cdr");
            log.error("===========================");
            return null;
        }


        try {
            subscriberService.validateSubscriber(ownerMsisdn);
        } catch (Exception ex) {
            log.error(ownerMsisdn);
            log.error(ex.getMessage());
            log.error("===========================");

            return null;
        }
        List<HrsRetrieveDto> changeValues = new ArrayList<>();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (int i = 0; i < cdrDtoList.size(); i++) {

            CdrDto cdrDto = cdrDtoList.get(i);
            Set<ConstraintViolation<CdrDto>> violations= validator.validate(cdrDto);
            if (violations.isEmpty()) {
                changeValues.add(processSingleCdr(cdrDto));
            }
            else{
                log.error(cdrDto.toString());
                violations.forEach(v ->
                        log.error("Validation error: {} ", v.getMessage())
                );
                log.error("===========================");
            }
        }

        return changeValues;

    }

    //Обрабатываем звонки
    private HrsRetrieveDto processSingleCdr(CdrDto cdrDto) {
        CallEntity callEntity = cdrService.callEntityFromCdr(cdrDto);

        try {
            callEntity.setSubscriber(subscriberService.monthTariffication(callEntity.getSubscriber(), callEntity.getStartCall()));
            log.info("monthly tariffication passed: " + callEntity.getSubscriber().getLastMonthTarifficationDate());
            log.error("===========================");
        } catch (Exception e) {
            log.info(e.getMessage());
            log.error("===========================");

        }

        HrsCallDto hrsCallDto = mapper.callEntityToHrsCallDto(callEntity);
        HrsRetrieveDto hrsRetrieveDto = hrsRest.hrsTarifficationCall(hrsCallDto);
        callRepository.saveAndFlush(callEntity);


        SubscriberEntity subscriber = subscriberService.changeBalanceCallTariffication(callEntity.getSubscriber(), hrsRetrieveDto);

        if (hrsRetrieveDto.getBalanceChange() != 0) {
            balanceChangesService.saveChangeEntity(hrsRetrieveDto, subscriber, callEntity.getEndCall().plusMinutes(2));
        }

        return hrsRetrieveDto;
    }
}
//TODO: помесячная тарификация