package brtApp.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.template.queue}")
    String cdrQueueName;

    @Value("${spring.rabbitmq.template.exchange}")
    String cdrExchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    String cdrRoutingKey;

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Queue myQueue() {
        return new Queue(cdrQueueName, true);
    }

    @Bean
    public DirectExchange myExchange() {
        return new DirectExchange(cdrExchange);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(cdrRoutingKey);
    }


}