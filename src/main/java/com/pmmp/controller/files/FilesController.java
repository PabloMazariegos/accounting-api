package com.pmmp.controller.files;

import com.pmmp.controller.files.request.UploadFileRequest;
import com.pmmp.controller.files.service.FilesService;
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

    public FilesController(final FilesService filesService) {
        this.filesService = filesService;
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
