package brtApp.mapper;

import brtApp.dto.CdrDto;
import brtApp.dto.HrsCallDto;
import brtApp.entity.CallEntity;
import brtApp.entity.CallTypeEntity;
import brtApp.entity.SubscriberEntity;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import brtApp.repository.CallTypeRepository;
import brtApp.repository.SubscriberRepository;

import java.time.Duration;


@Mapper(componentModel = "spring")
public abstract class MyMapper {
    @Autowired
    protected SubscriberRepository subscriberRepository;
    @Autowired
    CallTypeRepository callTypeRepository;

    @Mapping(target = "minutes", source = ".", qualifiedByName = "calculateMinutesRoundedUp")
    @Mapping(target = "callType", source = "callType.id")
    @Mapping(target = "romashkaCall", source = "isRomashkaCall")
    @Mapping(target = "tariffId", source = "subscriber.tariff.id")
    @Mapping(target = "tariffBalance", source = "subscriber.tariffBalance")
    @Mapping(target = "balance", source = "subscriber.balance")
    public abstract HrsCallDto callEntityToHrsCallDto(CallEntity callEntity);

    @Named("calculateMinutesRoundedUp")
    int calculateMinutesRoundedUp(CallEntity call) {
        if (call.getStartCall() == null || call.getEndCall() == null) {
            return 0;
        }
        Duration duration = Duration.between(call.getStartCall(), call.getEndCall());
        long seconds = duration.getSeconds();
        // Округляем вверх: (секунды + 59) / 60
        return (int) ((seconds + 59) / 60);
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriber", source = ".", qualifiedByName = "findSubscriberByOwner")
    @Mapping(target = "opponentMsisdn", source = ".", qualifiedByName = "mapOpponent")
    @Mapping(target = "startCall", source = "startDate")
    @Mapping(target = "endCall", source = "endDate")
    @Mapping(target = "callType", source = "flag", qualifiedByName = "mapFlagToCallType")
    @Mapping(target = "totalCost", constant = "0.0")
    @Mapping(target = "isRomashkaCall", source = ".", qualifiedByName = "checkIsRomashkaCall")
    public abstract CallEntity cdrDtoToCallEntity(CdrDto cdrDto);

    // Находим абонента (owner) в БД
    @Named("findSubscriberByOwner")
    protected SubscriberEntity findSubscriberByOwner(CdrDto dto) {
        return subscriberRepository.findByMsisdn(dto.getOwner())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscriber not found with MSISDN: " + dto.getOwner()
                ));
    }

    // Определяем номер оппонента
    @Named("mapOpponent")
    protected String mapOpponent(CdrDto dto) {
        return dto.getFlag().equals("01") ? dto.getReceiver() : dto.getInitiator();
    }

    // Проверяем, является ли оппонент абонентом Ромашки
    @Named("checkIsRomashkaCall")
    protected boolean checkIsRomashkaCall(CdrDto dto) {
        String opponentMsisdn = dto.getFlag().equals("01") ? dto.getReceiver() : dto.getInitiator();
        return subscriberRepository.findByMsisdn(opponentMsisdn).isPresent();
    }

    // Заглушка для callType (адаптируйте под вашу логику)
    @Named("mapFlagToCallType")
    protected CallTypeEntity mapFlagToCallType(String flag) {
        CallTypeEntity callType = callTypeRepository.findById(flag.equals("01") ? 1L : 2L).orElseThrow();
        return callType;
    }


}
