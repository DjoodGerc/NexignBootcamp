package com.example.demo.services;

import com.example.demo.dto.CallDataDto;
import com.example.demo.dto.UdrDto;
import com.example.demo.entity.CallEntity;
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

    public List<UdrDto> UdrReportForAll(List<CallEntity> callEntities) {
        Map<String, CallDataDto> map = new HashMap<>();

        for (CallEntity cdr : callEntities) {
            if (cdr.getReceiving().getOperator().getId()==1) {
                CallDataDto receiver = map.getOrDefault(cdr.getReceiving().getMsisdn(), new CallDataDto(cdr.getReceiving().getMsisdn()));
                receiver.addIncoming(cdr.getStartCall(), cdr.getEndCall());
                map.put(cdr.getReceiving().getMsisdn(), receiver);
            }
            if (cdr.getInitiating().getOperator().getId()==1){
                CallDataDto initiator = map.getOrDefault(cdr.getInitiating().getMsisdn(), new CallDataDto(cdr.getInitiating().getMsisdn()));
                initiator.addOutcoming(cdr.getStartCall(), cdr.getEndCall());
                map.put(cdr.getInitiating().getMsisdn(), initiator);
            }
        }

        return mapper.cdListToUdrList(map.values().stream().toList());


    }

    public UdrDto UdrReport(List<CallEntity> cdrEntities, String msisdn) {
        CallDataDto res = new CallDataDto(msisdn);
        for (CallEntity cdr : cdrEntities) {
            if (cdr.getReceiving().getMsisdn().equals(msisdn)) {
                res.addIncoming(cdr.getStartCall(), cdr.getEndCall());

            } else {
                res.addOutcoming(cdr.getStartCall(), cdr.getEndCall());
            }
        }

        return mapper.callDataToUdr(res);


    }
}
