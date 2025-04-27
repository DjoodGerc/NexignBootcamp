package brtApp.restInteraction;

import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.exception.TarifficationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HrsRest {
    @Value("${hrs.service.url}")
    String hrsUrl;

    private final RestClient restClient = RestClient.create(hrsUrl);

    public HrsRetrieveDto hrsTarifficationCall(HrsCallDto hrsCallDto) {
        HrsRetrieveDto hrsRetrieveDto=restClient.post()
                .uri("http://localhost:8082/tarifficateCall")
                .body(hrsCallDto)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new TarifficationException(response.getStatusCode(),response.getStatusText());
                })
                .body(HrsRetrieveDto.class);
        return  hrsRetrieveDto;
    }

    public HrsRetrieveDto getMonthTariffFeeAndMinutes(long tariffId) {
        HrsRetrieveDto hrsRetrieveDto=restClient.get()
                .uri("http://localhost:8082/monthTariffication/"+tariffId)
                .retrieve()
                .onStatus(status -> status.value() >=400, (request, response) -> {
                    throw new TarifficationException(response.getStatusCode(),response.getStatusText());
                })
                .body(HrsRetrieveDto.class);
        return hrsRetrieveDto;
    }


}
