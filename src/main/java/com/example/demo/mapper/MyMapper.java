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

    public CdrDto cdrEntityToDto(CallEntity cdrEntity, String number) {
        String flag;
        if (cdrEntity.getInitiating().getMsisdn().equals(number)) {
            flag = "01";
        } else {
            flag = "02";
        }
        return new CdrDto(flag, cdrEntity.getInitiating().getMsisdn(), cdrEntity.getReceiving().getMsisdn(), cdrEntity.getStartCall().toLocalDateTime(), cdrEntity.getEndCall().toLocalDateTime());
    }

    public List<CdrDto> cdrEntityListToDto(List<CallEntity> cdrEntities, String number) {
        List<CdrDto> cdrDtos = new ArrayList<>();
        for (CallEntity cdrEntity : cdrEntities) {
            cdrDtos.add(cdrEntityToDto(cdrEntity, number));
        }
        return cdrDtos;
    }

    ;
}
