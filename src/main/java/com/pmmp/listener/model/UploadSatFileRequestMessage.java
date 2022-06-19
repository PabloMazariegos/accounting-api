package com.pmmp.listener.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UploadSatFileRequestMessage {
    private UUID uuid;
    private String action;
    private String source;
    private UUID satFileId;
}
