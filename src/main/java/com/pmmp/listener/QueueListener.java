package com.pmmp.listener;

import com.pmmp.exception.AbstractServiceException;
import com.pmmp.listener.model.QueueMessage;
import com.pmmp.listener.service.QueueMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class QueueListener {
    private final QueueMessageService queueMessageService;

    public QueueListener(final QueueMessageService queueMessageService) {
        this.queueMessageService = queueMessageService;
    }

    @RabbitListener(queues = "#{rabbitmq.queue.retry}")
    public void consumeMessage(final QueueMessage queueMessage) {
        final UUID uuid = queueMessage.getUuid();
        final String action = queueMessage.getAction();
        try {
            queueMessage.accept(queueMessageService);
        } catch (final AbstractServiceException exception) {
            log.error("The queue has thrown a for the message with uuid '" + uuid + "'.", exception);
            throw exception;
        } catch (final Exception exception) {
            log.error("The queue has thrown a exception for the message with uuid '" + uuid + "'.", exception);
            throw exception;
        }
    }
}
