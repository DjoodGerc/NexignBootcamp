package crmApp.service;

import crmApp.client.BrtClient;
import crmApp.client.HrsClient;
import crmApp.dto.*;
import crmApp.exception.ClientException;
import crmApp.exception.TariffIsNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    BrtClient brt;
    @Autowired
    HrsClient hrs;

    public BrtRetrieveSubsData hrsAddSubscriber(SubscriberCrmDto subscriberDataDto) throws Exception {
        return brt.addSubscriber(subscriberDataDto);
    }

    public BrtRetrieveSubsData hrsGetSubsFullInfo(String msisdn) {
        return brt.getSubsFullInfo(msisdn);
    }

    public BrtRetrieveSubsData hrsUpdateSubscriber(SubscriberCrmDto subscriberDataDto) {
        return brt.updateSubscriber(subscriberDataDto);
    }

    public DeleteStatusDto hrsDeleteSubscriber(String msisdn) {
        return brt.deleteSubscriber(msisdn);
    }

    public SubsTariffDto getSubsTariff(String msisdn) {
        System.out.println(111);
        BrtRetrieveSubsData subsData=brt.getSubsFullInfo(msisdn);
        List<HrsTariffInfo> hrsTariffInfoList=hrs.getAllTariffs();
        HrsTariffInfo subsTariff=hrs.getTariffById(subsData.getTariffId());

        List<TariffDto> availableTariffs= hrsTariffInfoList.stream().map(i->new TariffDto(i.getId(),i.getName())).collect(Collectors.toList());

        SubsTariffDto result=new SubsTariffDto(msisdn,new TariffDto(subsTariff.getId(),subsTariff.getName()),availableTariffs);
        return result;
    }

    public SubsTariffDto changeSubsTariff(String msisdn,Long tariffId){
        HrsTariffInfo hrsTariffInfo=hrs.getTariffById(tariffId);
        BrtRetrieveSubsData subsData;
        if(hrsTariffInfo.isActive()){
            subsData=brt.changeSubsTariff(msisdn,tariffId);
        }else{
            throw new TariffIsNotActiveException("Tariff is not Active");
        }
        List<HrsTariffInfo> hrsTariffInfoList=hrs.getAllTariffs();
        HrsTariffInfo subsTariff=hrs.getTariffById(subsData.getTariffId());

        List<TariffDto> availableTariffs= hrsTariffInfoList.stream().map(i->new TariffDto(i.getId(),i.getName())).collect(Collectors.toList());

        SubsTariffDto result=new SubsTariffDto(msisdn,new TariffDto(subsTariff.getId(),subsTariff.getName()),availableTariffs);
        return result;

    }

    public BrtRetrieveSubsData changeBalance(String msisdn, ChangeBalanceDto changeBalanceDto) {
        if(changeBalanceDto.getAmount()>0) {
            return brt.changeSubBalance(msisdn, changeBalanceDto);
        }else{
            throw new ClientException(HttpStatus.BAD_REQUEST,"Изменение баланса должно быть положительным");
        }
    }

    public boolean CheckAuthority(Authentication authentication, String requestMsisdn){
        if (authentication.getName().equals(requestMsisdn)||authentication.getName().equals("admin")){
            return true;
        }
        else throw new ClientException(HttpStatus.BAD_REQUEST,"Вы не можете получить доступ к частному ресурсу, если вы не менеджер или не владелец номера");
    }
}
