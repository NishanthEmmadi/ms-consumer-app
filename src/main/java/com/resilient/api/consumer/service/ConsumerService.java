package com.resilient.api.consumer.service;


import com.resilient.api.consumer.model.MessageRecordEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
@Slf4j
public class ConsumerService {

    @Autowired
    private MessageSender messageSender;


    public void process_message(MessageRecordEvent messageRecordEvent){

        try {

            /*
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            String uri = "http://mockurl.com/";

            restTemplate.postForObject(uri, messageRecordEvent, String.class);

             */

            throw new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE);


        } catch (HttpClientErrorException ex){

            if(messageRecordEvent.getRetryCount() <= 5) {

                    if(ex.getStatusCode() != HttpStatus.BAD_REQUEST && ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {

                        log.error("Encountered a retry exception, retrying message for {} time", messageRecordEvent.getRetryCount());

                        messageRecordEvent.setRetryCount(messageRecordEvent.getRetryCount()+1);
                        messageSender.publishEvent("consumer-exchange", "retry.key", messageRecordEvent);
                    } else{
                        redirectToErrorQueue(messageRecordEvent);
                    }

            }else {
                log.error("Message failed to process after {} times retires", messageRecordEvent.getRetryCount());
                redirectToErrorQueue(messageRecordEvent);

            }

        } catch (Exception e) {

            log.error("Encountered a non retryable exception for the record {}", messageRecordEvent);
            redirectToErrorQueue(messageRecordEvent);

        }

    }

    private void redirectToErrorQueue(MessageRecordEvent messageRecordEvent) {

        messageSender.publishEvent("consumer-exchange","error.key", messageRecordEvent);
    }

}
