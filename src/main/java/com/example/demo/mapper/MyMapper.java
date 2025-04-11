package com.example.demo.mapper;

import com.example.demo.dto.CallDataDto;
import com.example.demo.dto.CdrDto;
import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CallEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * MapStruct маппер для удобства приведения классов
 */
@Component
@Mapper(componentModel = "spring")
public abstract class MyMapper {

    public UdrDto callDataToUdr(CallDataDto callDataDto) {
        return new UdrDto(callDataDto.getMsisdn(), callDataDto.getIncomingDuration(), callDataDto.getOutcomingDuration());
    }

    abstract public List<UdrDto> cdListToUdrList(List<CallDataDto> callDataDtos);

    public CdrDto callEntityToDto(CallEntity callEntity, String number) {
        String flag;
        if (callEntity.getInitiating().getMsisdn().equals(number)) {
            flag = "01";
        } else {
            flag = "02";
        }
        return new CdrDto(flag, callEntity.getInitiating().getMsisdn(), callEntity.getReceiving().getMsisdn(), callEntity.getStartCall().toLocalDateTime(), callEntity.getEndCall().toLocalDateTime());
    }

    public List<CdrDto> callEntityListToDto(List<CallEntity> cdrEntities, String number) {
        List<CdrDto> cdrDtos = new ArrayList<>();
        for (CallEntity callEntity : cdrEntities) {
            cdrDtos.add(callEntityToDto(callEntity, number));
        }
        return cdrDtos;
    }

    ;
}
