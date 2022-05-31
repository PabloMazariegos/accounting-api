package com.pmmp.listener.service;

import com.pmmp.listener.model.UploadSatFileMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueMessageService {
    private final UploadSatFileMessageService uploadSatFileMessageService;

    public QueueMessageService(final UploadSatFileMessageService uploadSatFileMessageService) {
        this.uploadSatFileMessageService = uploadSatFileMessageService;
    }

    public void process(final UploadSatFileMessage uploadSatFileMessage) {
        uploadSatFileMessageService.process(uploadSatFileMessage);
    }
}
