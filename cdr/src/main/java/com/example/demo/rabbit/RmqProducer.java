package com.example.demo.rabbit;

import com.example.demo.dto.CdrDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RmqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;
    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;
    public List<CdrDto> sendJsonMassage(List<CdrDto> cdrDtos){
        rabbitTemplate.convertAndSend(exchange,routingKey, cdrDtos);
        return cdrDtos;
    }

}