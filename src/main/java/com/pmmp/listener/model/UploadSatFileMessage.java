package com.pmmp.listener.model;

import com.pmmp.listener.service.UploadSatFileMessageService;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UploadSatFileMessage {
    private UUID uuid;
    private String action;
    private String source;
    private UUID satFileId;

    public void accept(final UploadSatFileMessageService visitor) {
        visitor.process(this);
    }
}
