package brtApp.rabbit;

import brtApp.Service.CallService;
import brtApp.Service.SubscriberService;
import brtApp.dto.CdrDto;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableRabbit
public class Listener {

    @Autowired
    SubscriberService subscriberService;
    @Autowired
    CallService callService;

    @RabbitListener(queues = "${spring.rabbitmq.template.queue}")
    public void listener(@Payload List<CdrDto> cdrDtos) throws Exception {
        callService.processCdrList(cdrDtos);
    }


}
