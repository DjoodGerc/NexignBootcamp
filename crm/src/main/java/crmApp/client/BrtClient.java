package crmApp.client;

import crmApp.dto.BrtRetrieveSubsData;
import crmApp.dto.DeleteStatusDto;
import crmApp.dto.SubscriberCrmDto;
import crmApp.exception.ClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class BrtClient {
    @Value("${brt.service.url}")
    String brtUrl;

    private final RestClient restClient = RestClient.create(brtUrl);


    public BrtRetrieveSubsData addSubscriber(SubscriberCrmDto subscriberDataDto) throws Exception{
        BrtRetrieveSubsData brtRetrieveSubsData=restClient.post()
                .uri(brtUrl+"/addSubscriber")
                .body(subscriberDataDto)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(),response.getStatusText());
                })
                .body(BrtRetrieveSubsData.class);
        return  brtRetrieveSubsData;
    }

    public BrtRetrieveSubsData getSubsFullInfo(String msisdn) {
        BrtRetrieveSubsData brtRetrieveSubsData=restClient.get()
                .uri(brtUrl+"/getSubscriberFullInfo/"+msisdn)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(),response.getStatusText());
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }

    public BrtRetrieveSubsData updateSubscriber(SubscriberCrmDto subscriberDataDto) {
        BrtRetrieveSubsData brtRetrieveSubsData=restClient.patch()
                .uri(brtUrl+"/updateSubscriber")
                .body(subscriberDataDto)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(),response.getStatusText());
                })
                .body(BrtRetrieveSubsData.class);
        return  brtRetrieveSubsData;

    }

    public DeleteStatusDto deleteSubscriber(String msisdn) {
        DeleteStatusDto deleteStatusDto=restClient.delete()
                .uri(brtUrl+"/deleteSubscriber/"+msisdn)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(),response.getStatusText());
                })
                .body(DeleteStatusDto.class);
        return deleteStatusDto;
    }


    public BrtRetrieveSubsData changeSubsTariff(String msisdn, Long newTariffId) {
        BrtRetrieveSubsData brtRetrieveSubsData=restClient.patch()
                .uri(brtUrl+"/changeSubsTariff/"+msisdn+"/"+newTariffId)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(),response.getStatusText());
                })
                .body(BrtRetrieveSubsData.class);
        return brtRetrieveSubsData;
    }
}
