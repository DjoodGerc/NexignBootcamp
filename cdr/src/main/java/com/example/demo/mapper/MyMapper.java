package com.example.demo.mapper;

import com.example.demo.dto.CallDataDto;
import com.example.demo.dto.CdrDto;
import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CallEntity;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * MapStruct маппер для удобства приведения классов
 */
@Component
@Mapper(componentModel = "spring")
public interface MyMapper {

    @Mapping(source = "msisdn", target = "msisdn")
    @Mapping(source = "incomingDuration", target = "incomingCall.totalTime", qualifiedByName = "formatDuration")
    @Mapping(source = "outcomingDuration", target = "outcomingCall.totalTime", qualifiedByName = "formatDuration")
    UdrDto callDataToUdr(CallDataDto callDataDto);


    @Named("formatDuration")
    default String formatDuration(Duration duration) {
        return DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss");
    }


    List<UdrDto> cdListToUdrList(List<CallDataDto> callDataDtos);

    @Mapping(source = "callEntity.initiating.msisdn", target = "initiator")
    @Mapping(source = "callEntity.receiving.msisdn", target = "receiver")
    @Mapping(source = "callEntity.startCall", target = "startDate")
    @Mapping(source = "callEntity.endCall", target = "endDate")
    @Mapping(target = "flag", expression = "java(determineFlag(callEntity.getInitiating().getMsisdn(), number))")
    CdrDto callEntityToDto(CallEntity callEntity, @Context String number);

    List<CdrDto> callEntityListToDto(List<CallEntity> cdrEntities, @Context String number);

    @Named("determineFlag")
    default String determineFlag(String callEntityMsisdn, @Context String number) {
        return callEntityMsisdn.equals(number) ? "01" : "02";
    }

}
