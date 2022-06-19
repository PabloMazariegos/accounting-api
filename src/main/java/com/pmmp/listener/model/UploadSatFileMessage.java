package com.pmmp.listener.model;

import com.pmmp.listener.service.QueueMessageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadSatFileMessage extends QueueMessage {
    private UUID satFileId;

    @Override
    public void accept(QueueMessageService visitor) {
        visitor.process(this);
    }
}
