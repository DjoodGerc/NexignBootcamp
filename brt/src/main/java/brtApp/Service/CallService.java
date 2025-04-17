package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.CallEntity;
import brtApp.entity.SubscriberEntity;
import brtApp.repository.BalanceChangesRepository;
import brtApp.repository.ChangeTypeRepository;
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
    public List<HrsRetrieveDto> processCdrList(List<CdrDto> cdrDtoList) {
        String ownerMsisdn = cdrDtoList.get(0).getOwner();
        try {
            subscriberService.validateSubscriber(ownerMsisdn);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return null;
        }
        List<HrsRetrieveDto> changeValues =new ArrayList<>();
        for (CdrDto cdrDto: cdrDtoList){
            changeValues.add(processSingleCdr(cdrDto));
        }

        return changeValues;

    }

    private HrsRetrieveDto processSingleCdr(CdrDto cdrDto) {


        CallEntity callEntity=mapper.cdrDtoToCallEntity(cdrDto);
        HrsCallDto hrsCallDto=mapper.callEntityToHrsCallDto(callEntity);
        HrsRetrieveDto hrsRetrieveDto=hrsRest.hrsProcessCall(hrsCallDto);
        SubscriberEntity subscriber=subscriberService.changeBalance(callEntity.getSubscriber(),hrsRetrieveDto);

        callRepository.saveAndFlush(callEntity);

        balanceChangesService.saveChangeEntity(hrsRetrieveDto,subscriber);

        return hrsRetrieveDto;

    }


}
//ToDO: разобраться какое время ставим в изменениях и как обрабатывать помесячные списания
