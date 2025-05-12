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
public interface MyMapper {


    @Mapping(target = "minutes", source = ".", qualifiedByName = "calculateMinutesRoundedUp")
    @Mapping(target = "callType", source = "callType.id")
    @Mapping(target = "isRomashkaCall", source = "isRomashkaCall")
    @Mapping(target = "tariffId", source = "subscriber.tariffId")
    @Mapping(target = "tariffBalance", source = "subscriber.tariffBalance")
    @Mapping(target = "balance", source = "subscriber.balance")
    HrsCallDto callEntityToHrsCallDto(CallEntity callEntity);

    @Named("calculateMinutesRoundedUp")
    default int calculateMinutesRoundedUp(CallEntity call) {
        if (call.getStartCall() == null || call.getEndCall() == null) {
            return 0;
        }
        Duration duration = Duration.between(call.getStartCall(), call.getEndCall());
        long seconds = duration.getSeconds();
        // Округляем вверх: (секунды + 59) / 60
        return (int) ((seconds + 59) / 60);
    }


}
