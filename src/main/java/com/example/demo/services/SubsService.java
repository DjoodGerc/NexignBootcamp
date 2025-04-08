package com.example.demo.services;

import com.example.demo.entity.SubscriberEntity;
import org.springframework.stereotype.Service;

@Service
public class SubsService {
    public boolean checkForRomashka(SubscriberEntity subscriberEntity){
        return subscriberEntity.getOperator().getId()==1;
    }
}
