package crmApp.client;

import crmApp.dto.BrtRetrieveSubsData;
import crmApp.dto.HrsTariffInfo;
import crmApp.exception.ClientException;
import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class HrsClient {

    @Value("${hrs.service.url}")
    String hrsUrl;

    RestClient restClient=RestClient.create(hrsUrl);


    public List<HrsTariffInfo> getAllTariffs() {
        List<HrsTariffInfo> hrsTariffInfos = restClient.get()
                .uri(hrsUrl + "/getAllTariffs")
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(), response.getStatusText());
                })
                .body(new ParameterizedTypeReference<List<HrsTariffInfo>>() {});

        return hrsTariffInfos;
    }

    public HrsTariffInfo getTariffById(long id) {
        HrsTariffInfo hrsTariffInfos = restClient.get()
                .uri(hrsUrl + "/getTariffById/"+id)
                .retrieve()
                .onStatus(status -> status.value() >= 400, (request, response) -> {
                    throw new ClientException(response.getStatusCode(), response.getStatusText());
                })
                .body(HrsTariffInfo.class);

        return hrsTariffInfos;
    }


}
