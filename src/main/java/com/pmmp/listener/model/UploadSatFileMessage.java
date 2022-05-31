package com.pmmp.listener.model;

import com.pmmp.listener.service.QueueMessageService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UploadSatFileMessage {
    private UUID satFileId;
    private UUID uuid;
    private String action;
    private String source;

    public void accept(final QueueMessageService visitor) {
        visitor.process(this);
    }
}
