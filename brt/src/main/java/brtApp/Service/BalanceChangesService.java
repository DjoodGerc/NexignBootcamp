package brtApp.Service;

import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.BalanceChangesEntity;
import brtApp.entity.ChangeTypeEntity;
import brtApp.entity.SubscriberEntity;
import brtApp.repository.BalanceChangesRepository;
import brtApp.repository.ChangeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BalanceChangesService {
    @Autowired
    ChangeTypeRepository changeTypeRepository;
    @Autowired
    BalanceChangesRepository balanceChangesRepository;
    public BalanceChangesEntity saveChangeEntity(HrsRetrieveDto hrsRetrieveDto, SubscriberEntity subscriberEntity,LocalDateTime date) {
        BalanceChangesEntity balanceChangesEntity = new BalanceChangesEntity();
        ChangeTypeEntity changeTypeEntity = changeTypeRepository.findById(hrsRetrieveDto.getBalanceChange()> 0.0d ? 1L : 2L).orElseThrow();

        balanceChangesEntity.setValue(hrsRetrieveDto.getBalanceChange());
        balanceChangesEntity.setDate(date);
        balanceChangesEntity.setSubscriber(subscriberEntity);
        balanceChangesEntity.setChangeType(changeTypeEntity);

        return balanceChangesRepository.saveAndFlush(balanceChangesEntity);


    }
}
