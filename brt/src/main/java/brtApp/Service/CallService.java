package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.CdrFlagEnum;
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


    //обрабатываем то что пришло из рэббита + логируем любые особенности
    public List<HrsRetrieveDto> processCdrList(List<CdrDto> cdrDtoList) {
        String ownerMsisdn = "";

        //ищем владельца cdr
        for (int i = 0; i < cdrDtoList.size(); i++) {
            if (cdrDtoList.get(i).getFlag().equals(CdrFlagEnum.INCOMING.getFlagId()) || cdrDtoList.get(i).getFlag().equals(CdrFlagEnum.OUTGOING.getFlagId())) {
                ownerMsisdn = cdrDtoList.get(i).getOwner();
            }
        }
        //если не нашли заявляем об этом,
        // тут можно с этой порченой cdr что то делать,
        // например складывать весь мусор в еще одну очередь в рэббите, но
        if (ownerMsisdn.isEmpty()) {

            log.error("Not found valid flag in any cdr");
            log.error("===========================");
            return null;
        }

        // наш/не наш абонент
        try {
            subscriberService.validateSubscriber(ownerMsisdn);
        } catch (Exception ex) {
            //не наш(
            log.error(ownerMsisdn);
            log.error(ex.getMessage());
            log.error("===========================");

            return null;
        }
        List<HrsRetrieveDto> changeValues = new ArrayList<>();
        //валидируем записи cdr, если нашли ощибки логируем их, строку не обрабатываем,
        // если все ок тарифицируем и едем дальше
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        for (int i = 0; i < cdrDtoList.size(); i++) {

            CdrDto cdrDto = cdrDtoList.get(i);
            Set<ConstraintViolation<CdrDto>> violations = validator.validate(cdrDto);
            if (violations.isEmpty()) {
                changeValues.add(processSingleCdr(cdrDto));
            } else {
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

        //пробуем провести помесячную тарификацию
        try {
            callEntity.setSubscriber(subscriberService.monthTariffication(callEntity.getSubscriber(), callEntity.getStartCall()));
            log.info("monthly tariffication passed: " + callEntity.getSubscriber().getLastMonthTarifficationDate());
            log.error("===========================");
        } catch (Exception e) {
            log.info(e.getMessage());
            log.error("===========================");

        }

        //получаем данные о тарификации
        HrsCallDto hrsCallDto = mapper.callEntityToHrsCallDto(callEntity);
        HrsRetrieveDto hrsRetrieveDto = hrsRest.hrsTarifficationCall(hrsCallDto);
        callRepository.saveAndFlush(callEntity);

        //меняем баланс (если необходимо)
        SubscriberEntity subscriber = subscriberService.changeBalanceCallTariffication(callEntity.getSubscriber(), hrsRetrieveDto);

        if (hrsRetrieveDto.getBalanceChange() != 0) {
            balanceChangesService.saveChangeEntity(hrsRetrieveDto.getBalanceChange(), subscriber, callEntity.getEndCall().plusMinutes(2));
        }

        return hrsRetrieveDto;
    }
}
//TODO: помесячная тарификация