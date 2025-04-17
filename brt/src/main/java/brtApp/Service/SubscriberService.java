package brtApp.Service;

import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.SubscriberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import brtApp.repository.SubscriberRepository;

@Service
public class SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;

    public boolean validateSubscriber(String msisdn){
        if (!subscriberRepository.findByMsisdn(msisdn).isPresent()){
            throw new RuntimeException("Not Romashka subscriber");
        }
        return true;
    }
    public SubscriberEntity changeBalance(SubscriberEntity subscriberEntity, HrsRetrieveDto hrsRetrieveDto){
        subscriberEntity.changeBalance(hrsRetrieveDto.getBalanceChange());
        subscriberEntity.changeBalance(hrsRetrieveDto.getTariffBalanceChange());
        return subscriberRepository.saveAndFlush(subscriberEntity);
    }

}
