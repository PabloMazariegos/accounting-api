package com.pmmp.controller.files.request;

import com.pmmp.model.enums.DocumentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

;

@Getter
@Setter
public class UploadFileRequest {
    @NotNull
    private String file;

    @NotNull
    private String fileName;

    @NotNull
    @Enumerated(STRING)
    private DocumentType documentTypeSlug;
}
