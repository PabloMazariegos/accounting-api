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

    @Bean
    public DirectExchange retryExchange(final RabbitMQProperties properties) {
        final String retryExchangeName = properties.getExchange().getRetry();
        return new DirectExchange(retryExchangeName);
    }

    @Bean
    public DirectExchange deadLetterExchange(final RabbitMQProperties properties) {
        final String deadExchangeName = properties.getExchange().getDead();

        return new DirectExchange(deadExchangeName);
    }

    @Bean
    public Queue deadLetterQueue(final RabbitMQProperties properties) {
        final String deadQueueName = properties.getQueue().getDead();

        return QueueBuilder.durable(deadQueueName).build();
    }

    @Bean
    public Queue retryableQueue(final RabbitMQProperties properties) {
        final String retryQueueName = properties.getQueue().getRetry();
        final String deadExchangeName = properties.getExchange().getDead();
        final String deadRoutingName = properties.getRouting().getDead();

        return QueueBuilder.durable(retryQueueName)
                .withArgument("x-dead-letter-exchange", deadExchangeName)
                .withArgument("x-dead-letter-routing-key", deadRoutingName)
                .build();
    }

    @Bean
    public Binding deadLetterBinding(final RabbitMQProperties properties) {
        final String deadRoutingName = properties.getRouting().getDead();
        return BindingBuilder.bind(deadLetterQueue(properties)).to(deadLetterExchange(properties)).with(deadRoutingName);
    }

    @Bean
    public Binding retryBinding(final RabbitMQProperties properties) {
        final String retryRoutingName = properties.getRouting().getRetry();
        return BindingBuilder.bind(retryableQueue(properties)).to(retryExchange(properties)).with(retryRoutingName);
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
