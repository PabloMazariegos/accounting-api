package com.pmmp.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RETRY_QUEUE_NAME = "r-accounting-queue";
    public static final String DEAD_QUEUE_NAME = "d-accounting-queue";

    public static final String RETRY_EXCHANGE_NAME = "r-accounting-exchange";
    public static final String DEAD_EXCHANGE_NAME = "d-accounting-exchange";

    public static final String RETRY_ROUTING_KEY = "r-accounting_message";
    public static final String DEAD_ROUTING_KEY = "d-accounting_message";


    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_EXCHANGE_NAME);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    @Bean
    public Queue retryableQueue() {
        return QueueBuilder.durable(RETRY_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", DEAD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_ROUTING_KEY);
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryableQueue()).to(retryExchange()).with(RETRY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(final ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}
