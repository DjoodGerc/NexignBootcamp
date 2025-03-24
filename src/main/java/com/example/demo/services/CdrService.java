package com.example.demo.services;

import com.example.demo.dto.CdrDto;
import com.example.demo.entity.CdrEntity;
import com.example.demo.mapper.MyMapper;
import com.example.demo.repository.CdrRepo;
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
    CdrRepo cdrRepo;
    @Autowired
    MyMapper mapper;

    public String cdrReport(long uid, String number, LocalDateTime startDateLocal, LocalDateTime endDateLocal) throws IOException {
        Timestamp startDate = Timestamp.valueOf(startDateLocal);
        Timestamp endDate = Timestamp.valueOf(endDateLocal);
        List<CdrEntity> cdrEntities = cdrRepo.findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(uid, startDate, endDate, uid, startDate, endDate);
        List<CdrDto> cdrDtos = mapper.cdrEntityListToDto(cdrEntities, number);
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
