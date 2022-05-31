package com.pmmp.listener;

import com.pmmp.exception.AbstractServiceException;
import com.pmmp.listener.model.UploadSatFileMessage;
import com.pmmp.listener.service.QueueMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.pmmp.config.rabbitmq.RabbitMQConfig.QUEUE_NAME;

@Slf4j
@Component
public class QueueListener {
    private final QueueMessageService queueMessageService;

    public QueueListener(final QueueMessageService queueMessageService) {
        this.queueMessageService = queueMessageService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void consumeMessage(@Payload final UploadSatFileMessage queueMessage) {
        final UUID uuid = queueMessage.getUuid();
        final String action = queueMessage.getAction();

        try {
            queueMessage.accept(queueMessageService);
        } catch (final AbstractServiceException exception) {
            log.error("The queue '" + QUEUE_NAME + "' has thrown a non-retryable exception for the message with uuid '" + uuid + "'.", exception);
        } catch (final Exception exception) {
            log.error("The queue '" + QUEUE_NAME + "' has thrown a non-retryable exception for the message with uuid '" + uuid + "'.", exception);
            throw exception;
        }
    }
}
