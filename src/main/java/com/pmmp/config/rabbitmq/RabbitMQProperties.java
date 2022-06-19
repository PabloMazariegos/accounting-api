package com.pmmp.config.rabbitmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("rabbitmq")
@ConfigurationProperties(prefix = "rabbitmq.properties")
@Data
public class RabbitMQProperties {
    Queue queue;
    Exchange exchange;
    Routing routing;

    @Data
    public static class Queue {
        String retry;
        String dead;
    }

    @Data
    public static class Exchange {
        String retry;
        String dead;
    }

    @Data
    public static class Routing {
        String retry;
        String dead;
    }
}
