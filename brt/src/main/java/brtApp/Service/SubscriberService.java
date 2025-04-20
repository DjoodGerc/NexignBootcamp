package brtApp.Service;

import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.SubscriberEntity;
import brtApp.restInteraction.HrsRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import brtApp.repository.SubscriberRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    BalanceChangesService balanceChangesService;
    @Autowired
    HrsRest hrsRest;

    public boolean validateSubscriber(String msisdn) {
        if (!subscriberRepository.findByMsisdn(msisdn).isPresent()) {
            throw new RuntimeException("Not Romashka subscriber");
        }
        return true;
    }

    //Изменение баланса
    public SubscriberEntity changeBalance(SubscriberEntity subscriberEntity, HrsRetrieveDto hrsRetrieveDto) {
        subscriberEntity.changeBalance(hrsRetrieveDto.getBalanceChange());
        subscriberEntity.changeTariffBalance(hrsRetrieveDto.getTariffBalanceChange());
        return subscriberRepository.saveAndFlush(subscriberEntity);
    }

    //Обработка помесячной тарификации
    public SubscriberEntity monthTariffication(SubscriberEntity subscriber, LocalDateTime startCall) {
        Optional<LocalDateTime> optional = checkMonthTarifficationDate(startCall, subscriber.getLastMonthTarifficationDate());
        if (optional.isEmpty()) {
            throw new RuntimeException("it's too early for tariffication");
        } else {
            LocalDateTime newDate = optional.get();
            subscriber.setLastMonthTarifficationDate(newDate);
            HrsRetrieveDto hrsRetrieveDto = hrsRest.getMonthTariffFeeAndMinutes(subscriber.getTariff().getId());
            subscriber = changeBalance(subscriber, hrsRetrieveDto);
            balanceChangesService.saveChangeEntity(hrsRetrieveDto, subscriber, newDate);
        }
        return subscriber;
    }


    //Проверяем пора ли тарифицировать и устанавливаем новую дату
    private Optional<LocalDateTime> checkMonthTarifficationDate(LocalDateTime startCall, LocalDateTime lastTariffication) {

        //если с даты последней тарификации не прошел месяц
        if (startCall.isBefore(lastTariffication.plusMonths(1))) {
            return Optional.empty();
        }

        long monthsPassed = ChronoUnit.MONTHS.between(lastTariffication, startCall);

        LocalDateTime newTarifficationDate = lastTariffication.plusMonths(monthsPassed);
        return Optional.of(newTarifficationDate);
    }

}
