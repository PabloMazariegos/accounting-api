package com.pmmp.controller;

import com.pmmp.model.request.UploadFileRequest;
import com.pmmp.service.FilesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/v1/files")
public class FilesController {
    private final FilesService filesService;

    public FilesController(final FilesService filesService1) {
        this.filesService = filesService1;
    }

    @PostMapping(value = "/upload")
    @ResponseStatus(OK)
    public void uploadFiles(@RequestBody @Valid UploadFileRequest uploadFileRequest) {

        filesService.processFile(
                uploadFileRequest.getFile(),
                uploadFileRequest.getFileName(),
                uploadFileRequest.getDocumentTypeSlug()
        );
    }
}
