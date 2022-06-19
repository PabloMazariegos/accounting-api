package com.pmmp.controller;

import com.pmmp.model.request.UploadFileRequest;
import com.pmmp.service.SalesService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = "/v1/sales")
public class SalesController {
    private final SalesService salesService;

    public SalesController(final SalesService salesService1) {
        this.salesService = salesService1;
    }

    @PostMapping(value = "/upload")
    @ResponseStatus(NO_CONTENT)
    public void uploadSalesFile(@RequestBody @Valid UploadFileRequest uploadFileRequest) {

        salesService.processFile(
                uploadFileRequest.getFile(),
                uploadFileRequest.getFileName(),
                uploadFileRequest.getDocumentTypeSlug()
        );
    }
}
