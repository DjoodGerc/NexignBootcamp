package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.CallEntity;
import brtApp.entity.SubscriberEntity;
import brtApp.exception.TarifficationException;
import lombok.extern.slf4j.Slf4j;
import brtApp.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import brtApp.repository.CallRepository;
import brtApp.restInteraction.HrsRest;

import java.util.ArrayList;
import java.util.List;

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
        String ownerMsisdn = cdrDtoList.get(0).getOwner();
        try {
            subscriberService.validateSubscriber(ownerMsisdn);

        } catch (Exception ex) {
            log.info(ex.getMessage());
            return null;
        }
        List<HrsRetrieveDto> changeValues = new ArrayList<>();
        for (CdrDto cdrDto : cdrDtoList) {
//            try {
            changeValues.add(processSingleCdr(cdrDto));
//            }
//            catch (TarifficationException tarifficationException){
//                log.warn(tarifficationException.getMessage());
//            }


        }

        return changeValues;

    }

    //Обрабатываем звонки
    private HrsRetrieveDto processSingleCdr(CdrDto cdrDto) {
        CallEntity callEntity = cdrService.callEntityFromCdr(cdrDto);

        try {
            callEntity.setSubscriber(subscriberService.monthTariffication(callEntity.getSubscriber(), callEntity.getStartCall()));
            log.info("monthly tariffication passed: " + callEntity.getSubscriber().getLastMonthTarifficationDate());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        HrsCallDto hrsCallDto = mapper.callEntityToHrsCallDto(callEntity);
        HrsRetrieveDto hrsRetrieveDto = hrsRest.hrsTarifficationCall(hrsCallDto);
        callRepository.saveAndFlush(callEntity);


        SubscriberEntity subscriber= subscriberService.changeBalanceCallTariffication(callEntity.getSubscriber(), hrsRetrieveDto);

        if (hrsRetrieveDto.getBalanceChange()!=0) {
            balanceChangesService.saveChangeEntity(hrsRetrieveDto, subscriber, callEntity.getEndCall().plusMinutes(2));
        }

        return hrsRetrieveDto;
    }
}
//TODO: помесячная тарификация