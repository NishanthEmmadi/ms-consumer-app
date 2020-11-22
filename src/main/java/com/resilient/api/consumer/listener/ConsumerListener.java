package com.resilient.api.consumer.listener;


import com.resilient.api.consumer.model.MessageRecordEvent;
import com.resilient.api.consumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerListener {

    private ConsumerService consumerService;


    public ConsumerListener(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @RabbitListener(queues = "source.v1")
    public void process(MessageRecordEvent messageRecordEvent){

        log.info("Polled message from source queue {}", messageRecordEvent);
        consumerService.process_message(messageRecordEvent);

    }
}
