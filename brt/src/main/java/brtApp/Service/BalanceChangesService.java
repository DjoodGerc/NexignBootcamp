package brtApp.Service;

import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.BalanceChangeEnum;
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

    public BalanceChangesEntity saveChangeEntity(Double balanceChange, SubscriberEntity subscriberEntity, LocalDateTime date) {
        BalanceChangesEntity balanceChangesEntity;
        ChangeTypeEntity changeTypeEntity = changeTypeRepository.findById(balanceChange > 0.0d ? BalanceChangeEnum.INCREASE.getId() : BalanceChangeEnum.DECREASE.getId()).orElseThrow();

        balanceChangesEntity = BalanceChangesEntity.builder().
                value(balanceChange).
                date(date).
                subscriber(subscriberEntity).
                changeType(changeTypeEntity).build();


        return balanceChangesRepository.saveAndFlush(balanceChangesEntity);




    }
}
