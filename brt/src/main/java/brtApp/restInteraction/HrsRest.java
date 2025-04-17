package brtApp.restInteraction;

import brtApp.dto.HrsCallDto;
import brtApp.dto.HrsRetrieveDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HrsRest {
    @Value("${hrs.service.url}")
    String hrsUrl;

    private final RestClient restClient = RestClient.create(hrsUrl);

    public HrsRetrieveDto hrsProcessCall(HrsCallDto hrsCallDto){
        return new HrsRetrieveDto(0L,0d);
//        HrsRetrieveDto hrsRetrieveDto=restClient.post()
//                .uri("processCall")
//                .body(hrsCallDto)
//                .retrieve()
//                .body(HrsRetrieveDto.class);

    }


}
