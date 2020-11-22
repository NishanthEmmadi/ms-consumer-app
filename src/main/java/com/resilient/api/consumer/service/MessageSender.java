package com.resilient.api.consumer.service;


import com.resilient.api.consumer.model.MessageRecordEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {

    private RabbitTemplate rabbitTemplate;


    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(String exchange, String routingKey, MessageRecordEvent event){

        rabbitTemplate.convertAndSend(exchange,routingKey,event);

    }
}

