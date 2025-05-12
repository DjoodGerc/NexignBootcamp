package hrsApp.service;

import hrsApp.dto.HrsCallDto;
import hrsApp.dto.HrsFeeDto;
import hrsApp.entity.CallTypeEnum;
import hrsApp.entity.TariffEntity;
import hrsApp.entity.TariffTypeEnum;
import hrsApp.exception.MonthTarifficationIsNotAllowedForEventTariffException;
import hrsApp.repository.TariffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {

    @Autowired
    TariffRepository tariffRepository;

    //тарифицируем звонок
    public HrsFeeDto tarifficateCall(HrsCallDto hrsCallDto) {

        HrsFeeDto result = new HrsFeeDto();
        long callType = hrsCallDto.getCallType();
        boolean isRomashkaCall = hrsCallDto.isRomashkaCall();
        hrsCallDto.getBalance();
        long tariffMinutes = hrsCallDto.getTariffBalance();
        long callMinutes = hrsCallDto.getMinutes();
        TariffEntity tariff = getTariff(hrsCallDto.getTariffId());

        //безлимит
        if (tariff.getTariffParametr().getTariffType().getId() == TariffTypeEnum.UNLIMITED.getId()) {
            return result;
        }
        //поивентный
        else if (tariff.getTariffParametr().getTariffType().getId() == TariffTypeEnum.EVENT.getId()) {
            result.setBalanceChange(getCallFee(tariff, callType, isRomashkaCall, callMinutes));
        }
        //помесячный
        else if (tariff.getTariffParametr().getTariffType().getId() == TariffTypeEnum.MONTH.getId()) {
            if (callMinutes > tariffMinutes) {
                result.setTariffBalanceChange(-tariffMinutes);
                result.setBalanceChange(getCallFee(tariff, callType, isRomashkaCall, callMinutes - tariffMinutes));
                return result;
            } else {
                result.setTariffBalanceChange(-callMinutes);
            }
        }

        return result;
    }

    // получаем инфу о тарифе
    public TariffEntity getTariff(Long id) {
        return tariffRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Тариф с таким id не был найден"));
    }

    //получаем данные об оплате
    private double getCallFee(TariffEntity tariff, long callType, boolean isRomashkaCall, long minutes) {
        double callprice;
        if (callType == CallTypeEnum.INCOMING.getId()) {
            if (isRomashkaCall) {
                callprice = -tariff.getTariffParametr().getReceivingInternalCallCost() * minutes;
            } else {
                callprice = -tariff.getTariffParametr().getReceivingExternalCallCost() * minutes;
            }
        } else if (callType == CallTypeEnum.OUTGOING.getId()) {
            if (isRomashkaCall) {
                callprice = -tariff.getTariffParametr().getInitiatingInternalCallCost() * minutes;
            } else {
                callprice = -tariff.getTariffParametr().getInitiatingExternalCallCost() * minutes;
            }
        } else {
            throw new EntityNotFoundException("Неизвестный тип звонка");
        }
        return callprice;
    }

    //данные о помесячной тарификации
    public HrsFeeDto monthTariffication(Long id) throws MonthTarifficationIsNotAllowedForEventTariffException {
        TariffEntity tariff = tariffRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого тарифа не существует"));
        HrsFeeDto result = new HrsFeeDto();
        if (tariff.getTariffParametr().getTariffType().getId() == TariffTypeEnum.EVENT.getId()) {
            throw new MonthTarifficationIsNotAllowedForEventTariffException(HttpStatus.CONFLICT, "Помесячная тарификация не предрставляется для этого тарифа");
        } else {

            result.setBalanceChange(-tariff.getTariffParametr().getMonthlyFee());
            result.setTariffBalanceChange(tariff.getTariffParametr().getMonthlyMinuteCapacity());
        }
        return result;
    }

    //все тарифы
    public List<TariffEntity> getAllTariffs() {
        return tariffRepository.findAll();
    }
}
