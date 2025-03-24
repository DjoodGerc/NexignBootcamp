package com.example.demo.services;

import com.example.demo.dto.CallDataDto;
import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CdrEntity;
import com.example.demo.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * служебный класс для udr
 * cоздает отчеты по заданным параметрам
 */
@Service
public class UdrService {

    @Autowired
    MyMapper mapper;

    public List<UdrDto> UdrReportForAll(List<CdrEntity> cdrEntities) {
        Map<String, CallDataDto> map = new HashMap<>();
        for (int i = 0; i < cdrEntities.size(); i++) {

        }
        for (CdrEntity cdr : cdrEntities) {
            CallDataDto receiver = map.getOrDefault(cdr.getReceiving().getNumber(), new CallDataDto(cdr.getReceiving().getNumber()));
            receiver.addIncoming(cdr.getStartCall(), cdr.getEndCall());
            map.put(cdr.getReceiving().getNumber(), receiver);

            CallDataDto initiator = map.getOrDefault(cdr.getInitiating().getNumber(), new CallDataDto(cdr.getInitiating().getNumber()));
            initiator.addOutcoming(cdr.getStartCall(), cdr.getEndCall());
            map.put(cdr.getInitiating().getNumber(), initiator);
        }

        return mapper.cdListToUdrList(map.values().stream().toList());


    }

    public UdrDto UdrReport(List<CdrEntity> cdrEntities, String number) {
        CallDataDto res = new CallDataDto(number);
        for (CdrEntity cdr : cdrEntities) {
            if (cdr.getReceiving().getNumber().equals(number)) {
                res.addIncoming(cdr.getStartCall(), cdr.getEndCall());

            } else {
                res.addOutcoming(cdr.getStartCall(), cdr.getEndCall());
            }
        }

        return mapper.callDataToUdr(res);


    }
}
