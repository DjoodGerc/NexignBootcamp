package brtApp.client;

import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.dto.HrsTariffInfo;
import brtApp.exception.ClientException;
import brtApp.exception.TarifficationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class HrsRest {
    @Value("${hrs.service.url}")
    String hrsUrl;

    private final RestClient restClient = RestClient.create(hrsUrl);

    //тарификация звонков
    public HrsRetrieveDto hrsTarifficationCall(HrsCallDto hrsCallDto) {
        HrsRetrieveDto hrsRetrieveDto = restClient.post()
                .uri(hrsUrl + "/tarifficateCall")
                .body(hrsCallDto)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new TarifficationException(response.getStatusCode(), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(HrsRetrieveDto.class);
        return hrsRetrieveDto;
    }

    //помесячная тарификация
    public HrsRetrieveDto getMonthTariffFeeAndMinutes(long tariffId) {
        HrsRetrieveDto hrsRetrieveDto = restClient.get()
                .uri(hrsUrl + "/monthTariffication/" + tariffId)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new TarifficationException(response.getStatusCode(), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(HrsRetrieveDto.class);
        return hrsRetrieveDto;
    }

    //получение данных о конкретном тарифе
    public HrsTariffInfo getTariffById(Long id) {
        HrsTariffInfo hrsTariffInfos = restClient.get()
                .uri(hrsUrl + "/getTariffById/" + id)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(response.getStatusCode(), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(HrsTariffInfo.class);

        return hrsTariffInfos;
    }

}


