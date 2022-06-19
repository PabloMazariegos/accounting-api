package com.pmmp.listener.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pmmp.listener.service.QueueMessageService;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = NAME, property = "action", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UploadSatFileMessage.class, name = QueueMessage.UPLOAD_SAT_FILE)
})
public abstract class QueueMessage implements Serializable {
    public static final String UPLOAD_SAT_FILE = "UPLOAD_SAT_SALES_FILE";

    private UUID uuid;
    protected String action;
    protected String source;

    public abstract void accept(QueueMessageService visitor);
}
