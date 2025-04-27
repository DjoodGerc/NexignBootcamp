package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.CdrFlagEnum;
import brtApp.entity.CallEntity;
import brtApp.entity.CallTypeEntity;
import brtApp.entity.CallTypeFlagEnum;
import brtApp.entity.SubscriberEntity;
import brtApp.repository.CallTypeRepository;
import brtApp.repository.SubscriberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CdrService {
    @Autowired
    SubscriberService subscriberService;
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    CallTypeRepository callTypeRepository;

    public CallEntity callEntityFromCdr(CdrDto cdrDto) {
        SubscriberEntity subscriber = subscriberService.findSubscriberByOwner(cdrDto);

        String opponentMsisdn = mapOpponent(cdrDto);

        boolean isRomashkaCall = checkIsRomashkaCall(cdrDto);

        CallTypeEntity callType = mapFlagToCallType(cdrDto.getFlag());
        return CallEntity.builder()
                .subscriber(subscriber)
                .opponentMsisdn(opponentMsisdn)
                .startCall(cdrDto.getStartDate())
                .endCall(cdrDto.getEndDate())
                .callType(callType)
                .totalCost(0.0)
                .isRomashkaCall(isRomashkaCall)
                .build();
    }

    private String mapOpponent(CdrDto dto) {
        return dto.getFlag().equals(CdrFlagEnum.INCOMING.getFlagId()) ? dto.getReceiver() : dto.getInitiator();
    }

    private boolean checkIsRomashkaCall(CdrDto dto) {
        String opponentMsisdn = dto.getFlag().equals(CdrFlagEnum.INCOMING.getFlagId()) ? dto.getReceiver() : dto.getInitiator();
        return subscriberRepository.findByMsisdn(opponentMsisdn).isPresent();
    }

    private CallTypeEntity mapFlagToCallType(String flag) {
        return callTypeRepository.findById(
                        flag.equals(CdrFlagEnum.INCOMING.getFlagId()) ? CallTypeFlagEnum.INCOMING.getFlagId() : CallTypeFlagEnum.OUTGOING.getFlagId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "CallType not found for flag: " + flag));
    }

}
