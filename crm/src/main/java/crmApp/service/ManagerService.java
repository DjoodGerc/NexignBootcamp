package crmApp.service;

import crmApp.client.BrtClient;
import crmApp.client.HrsClient;
import crmApp.dto.*;
import crmApp.exception.TariffIsNotActiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {
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
}
