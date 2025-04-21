package brtApp.Service;

import brtApp.dto.CdrDto;
import brtApp.dto.HrsRetrieveDto;
import brtApp.entity.SubscriberEntity;
import brtApp.exception.NotRomashkaException;
import brtApp.exception.TooEarlyForTarifficationException;
import brtApp.restInteraction.HrsRest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import brtApp.repository.SubscriberRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Slf4j
public class SubscriberService {
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    BalanceChangesService balanceChangesService;
    @Autowired
    HrsRest hrsRest;

    public SubscriberEntity findSubscriberByOwner(CdrDto dto) {
        return subscriberRepository.findByMsisdn(dto.getOwner())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Subscriber not found with MSISDN: " + dto.getOwner()
                ));
    }

    public boolean validateSubscriber(String msisdn) {
        if (!subscriberRepository.findByMsisdn(msisdn).isPresent()) {
            throw new NotRomashkaException("Not Romashka subscriber");
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
    public SubscriberEntity monthTariffication(SubscriberEntity subscriber, LocalDateTime startCall)  {
        LocalDateTime newDate = checkMonthTarifficationDate(startCall, subscriber);

        subscriber.setLastMonthTarifficationDate(newDate);
        HrsRetrieveDto hrsRetrieveDto = hrsRest.getMonthTariffFeeAndMinutes(subscriber.getTariffId());
        subscriber = changeBalance(subscriber, hrsRetrieveDto);
        if (hrsRetrieveDto.getBalanceChange() > 0) {
            balanceChangesService.saveChangeEntity(hrsRetrieveDto, subscriber, newDate);
        }
        log.info("Month tariffication passed successfully");

        return subscriber;
    }


    //Проверяем пора ли тарифицировать и устанавливаем новую дату
    private LocalDateTime checkMonthTarifficationDate(LocalDateTime startCall, SubscriberEntity subscriber) {
        LocalDateTime lastTariffication=subscriber.getLastMonthTarifficationDate();
        if (lastTariffication == null){
            LocalDateTime registrationDate=subscriber.getRegistrationDate();
            long daysSinceRegistration = ChronoUnit.DAYS.between(registrationDate, startCall);
            long fullPeriods = daysSinceRegistration / 30; // количество полных 30-дневных периодов
            return registrationDate.plusDays(30 * fullPeriods);
        }
        else {
            if (startCall.isBefore(lastTariffication.plusDays(30))) {
                throw new TooEarlyForTarifficationException(
                        "It's too early for tariffication. Next tariffication available after: "
                                + lastTariffication.plusDays(30)
                );
            }

            long daysPassed = ChronoUnit.DAYS.between(lastTariffication, startCall);
            long fullPeriods = daysPassed / 30;

            return lastTariffication.plusDays(30 * fullPeriods);

        }
    }

}
