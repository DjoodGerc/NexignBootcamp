package com.example.demo.services;

import com.example.demo.dto.CdrDto;
import com.example.demo.entity.CallEntity;
import com.example.demo.mapper.MyMapper;
import com.example.demo.rabbit.RmqProducer;
import com.example.demo.repository.CallRepo;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * служебный класс для работы с cdr
 * CdrReport(params) - метод для генерации csv, по заданным параметрам
 */
@Service
public class CdrService {
    @Autowired
    CallRepo callRepo;
    @Autowired
    MyMapper mapper;
    @Autowired
    RmqProducer rmqProducer;

    public void sendCdrReport(String msisdn, List<CallEntity> callEntities){
        List<CdrDto> cdrDtos = mapper.callEntityListToDto(callEntities, msisdn);
//        System.out.println(cdrDtos);
        rmqProducer.sendJsonMassage(cdrDtos);
    }
    public String cdrReport(long uid, String number, LocalDateTime startDateLocal, LocalDateTime endDateLocal) throws IOException {
        Timestamp startDate = Timestamp.valueOf(startDateLocal);
        Timestamp endDate = Timestamp.valueOf(endDateLocal);
        List<CallEntity> cdrEntities = callRepo.findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(uid, startDate, endDate, uid, startDate, endDate);
        List<CdrDto> cdrDtos = mapper.callEntityListToDto(cdrEntities, number);
        CsvMapper mapper = new CsvMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        CsvSchema schema = mapper.schemaFor(CdrDto.class)
                .withColumnSeparator(',')
                .withLineSeparator("\n\n")
                .withoutQuoteChar()
                .sortedBy("flag", "initiator", "receiver", "startDate", "endDate");


        String uuid = UUID.randomUUID().toString();
        String fileName = number + "_" + uuid;
        ObjectWriter writer = mapper.writer(schema);

        writer.writeValue(new FileWriter("src/main/java/com/example/demo/reports/" + fileName + ".csv", StandardCharsets.UTF_8), cdrDtos);


        return fileName;

    }
}
