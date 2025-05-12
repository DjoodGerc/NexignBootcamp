package crmApp.client;

import crmApp.dto.BrtRetrieveSubsData;
import crmApp.dto.ChangeBalanceDto;
import crmApp.dto.DeleteStatusDto;
import crmApp.dto.SubscriberCrmDto;
import crmApp.exception.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class BrtClient {
    @Value("${brt.service.url}")
    String brtUrl;

    private final RestClient restClient = RestClient.create(brtUrl);


    public BrtRetrieveSubsData addSubscriber(SubscriberCrmDto subscriberDataDto) throws Exception {
        BrtRetrieveSubsData brtRetrieveSubsData = restClient.post()
                .uri(brtUrl + "/addSubscriber")
                .body(subscriberDataDto)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }

    public BrtRetrieveSubsData getSubsFullInfo(String msisdn) {
        BrtRetrieveSubsData brtRetrieveSubsData = restClient.get()
                .uri(brtUrl + "/getSubscriberFullInfo/" + msisdn)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }

    public BrtRetrieveSubsData updateSubscriber(SubscriberCrmDto subscriberDataDto) {
        BrtRetrieveSubsData brtRetrieveSubsData = restClient.patch()
                .uri(brtUrl + "/updateSubscriber")
                .body(subscriberDataDto)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;

    }

    public DeleteStatusDto deleteSubscriber(String msisdn) {
        DeleteStatusDto deleteStatusDto = restClient.delete()
                .uri(brtUrl + "/deleteSubscriber/" + msisdn)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(DeleteStatusDto.class);
        return deleteStatusDto;
    }


    public BrtRetrieveSubsData changeSubsTariff(String msisdn, Long newTariffId) {
        BrtRetrieveSubsData brtRetrieveSubsData = restClient.patch()
                .uri(brtUrl + "/changeSubsTariff/" + msisdn + "/" + newTariffId)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }

    public BrtRetrieveSubsData changeSubBalance(String msisdn, ChangeBalanceDto changeBalanceDto) {
        BrtRetrieveSubsData brtRetrieveSubsData = restClient.put()
                .uri(brtUrl + "/changeSubsBalance/" + msisdn)
                .body(changeBalanceDto)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    String body = new BufferedReader(
                            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    throw new ClientException(HttpStatus.valueOf(response.getStatusCode().value()), body.isEmpty() ? response.getStatusText() : body);
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }
}
