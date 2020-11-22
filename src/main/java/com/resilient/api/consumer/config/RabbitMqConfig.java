package com.resilient.api.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("consumer-exchange");
    }

    @Bean
    public Queue sourceQueue(){
        return new Queue("source.v1");
    }

    @Bean
    public Queue errorQueue(){
        return new Queue("error.v1");
    }

    /*

     Retry-able messages are published to the retry queue. where a retry queue is configured(X, Y) such that,
     the messages which are published to this queue are moved to the source queue and retried for X number
     of times for every Y interval of time.

     Retry count Y is managed by the code.

     */

    @Bean
    public Queue retryQueue(){
        return QueueBuilder.durable("retry.v1")
                           .withArgument("x-dead-letter-exchange","consumer-exchange")
                           .withArgument("x-dead-letter-routing-key","source.key")
                           .withArgument("x-message-ttl",3000).build();
    }

    @Bean
    public Binding bindErrorQueueToExchange(){
        return BindingBuilder.bind(errorQueue()).to(exchange()).with("error.key");
    }

    @Bean
    public Binding bindSourceQueueToExchange(){
        return BindingBuilder.bind(sourceQueue()).to(exchange()).with("source.key");
    }

    @Bean
    public Binding bindRetryQueueToExchange(){
        return BindingBuilder.bind(retryQueue()).to(exchange()).with("retry.key");
    }



}
