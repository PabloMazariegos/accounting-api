package com.pmmp.controller;

import com.pmmp.assembler.PurchaseResourceModelAssembler;
import com.pmmp.model.Purchase;
import com.pmmp.model.enums.RegisterType;
import com.pmmp.model.resource.PurchaseResourceModel;
import com.pmmp.service.PurchaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/v1/purchases")
public class PurchasesController {
    private final PurchaseService purchaseService;
    private final PurchaseResourceModelAssembler purchaseResourceModelAssembler;

    public PurchasesController(final PurchaseService purchaseService,
                               final PurchaseResourceModelAssembler purchaseResourceModelAssembler) {
        this.purchaseService = purchaseService;
        this.purchaseResourceModelAssembler = purchaseResourceModelAssembler;
    }

    @GetMapping
    @ResponseStatus(OK)
    public PagedModel<PurchaseResourceModel> getPurchases(@RequestParam(value = "from_date") @DateTimeFormat(iso = DATE) Date fromDate,
                                                          @RequestParam(value = "to_date") @DateTimeFormat(iso = DATE) Date toDate,
                                                          @RequestParam(value = "document_number", required = false) String documentNumber,
                                                          @RequestParam(value = "document_type", required = false) String documentType,
                                                          @RequestParam(value = "serial", required = false) String serial,
                                                          @RequestParam(value = "invoice_number", required = false) String invoiceNumber,
                                                          @RequestParam(value = "nit", required = false) String nit,
                                                          @RequestParam(value = "client_name", required = false) String clientName,
                                                          @RequestParam(value = "amount", required = false) BigDecimal amount,
                                                          @RequestParam(value = "iva_amount", required = false) BigDecimal ivaAmount,
                                                          @RequestParam(value = "register_type", required = false) RegisterType registerType,
                                                          @RequestParam(value = "sat_file_id", required = false) UUID satFileId,
                                                          @SortDefault(sort = "createdAt", direction = DESC) final Pageable pageable,
                                                          final PagedResourcesAssembler<Purchase> assembler) {

        final Page<Purchase> purchases = purchaseService.getPurchases(fromDate,
                toDate,
                documentNumber,
                documentType,
                serial,
                invoiceNumber,
                nit,
                clientName,
                amount,
                ivaAmount,
                registerType,
                satFileId,
                pageable);

        return assembler.toModel(purchases, purchaseResourceModelAssembler);
    }
}
