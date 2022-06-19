package com.pmmp.listener.service;

import com.pmmp.listener.model.UploadSatFileMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueMessageService {
    private final UploadSatFileMessageService uploadSatFileMessageService;
    private final RabbitTemplate rabbitTemplate;

    private static final String TYPE_ID_HEADER = "__TypeId__";

    public QueueMessageService(final UploadSatFileMessageService uploadSatFileMessageService,
                               final RabbitTemplate rabbitTemplate) {
        this.uploadSatFileMessageService = uploadSatFileMessageService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void process(final UploadSatFileMessage uploadSatFileMessage) {
        uploadSatFileMessageService.process(uploadSatFileMessage);
    }

    public void convertAndSend(String exchange, String routingKey, final Object object) {
        rabbitTemplate.convertAndSend(exchange, routingKey, object, message -> {
            message.getMessageProperties().getHeaders().remove(TYPE_ID_HEADER);
            return message;
        });
    }
}
