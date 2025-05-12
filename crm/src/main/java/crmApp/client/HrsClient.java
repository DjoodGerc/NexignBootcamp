package crmApp.client;

import crmApp.dto.BrtRetrieveSubsData;
import crmApp.dto.HrsTariffInfo;
import crmApp.exception.ClientException;
import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HrsClient {

    @Value("${hrs.service.url}")
    String hrsUrl;

    RestClient restClient = RestClient.create(hrsUrl);


    public List<HrsTariffInfo> getAllTariffs() {
        List<HrsTariffInfo> hrsTariffInfos = restClient.get()
                .uri(hrsUrl + "/getAllTariffs")
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(new ParameterizedTypeReference<List<HrsTariffInfo>>() {
                });

        return hrsTariffInfos;
    }

    public HrsTariffInfo getTariffById(long id) {
        HrsTariffInfo hrsTariffInfos = restClient.get()
                .uri(hrsUrl + "/getTariffById/" + id)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(HrsTariffInfo.class);

        return hrsTariffInfos;
    }


}
