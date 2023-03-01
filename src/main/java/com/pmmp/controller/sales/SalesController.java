package com.pmmp.controller.sales;

import com.pmmp.controller.sales.assembler.SaleResourceModelAssembler;
import com.pmmp.model.Sale;
import com.pmmp.model.enums.InvoiceStatus;
import com.pmmp.model.enums.RegisterType;
import com.pmmp.controller.sales.resource.SaleResourceModel;
import com.pmmp.controller.sales.service.SaleService;
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
@RequestMapping(value = "/v1/sales")
public class SalesController {
    private final SaleService saleService;
    private final SaleResourceModelAssembler saleResourceModelAssembler;

    public SalesController(final SaleService saleService, final SaleResourceModelAssembler saleResourceModelAssembler) {
        this.saleService = saleService;
        this.saleResourceModelAssembler = saleResourceModelAssembler;
    }

    @GetMapping
    @ResponseStatus(OK)
    public PagedModel<SaleResourceModel> getSales(@RequestParam(value = "from_date") @DateTimeFormat(iso = DATE) Date fromDate,
                                                  @RequestParam(value = "to_date") @DateTimeFormat(iso = DATE) Date toDate,
                                                  @RequestParam(value = "document_number", required = false) String documentNumber,
                                                  @RequestParam(value = "serial", required = false) String serial,
                                                  @RequestParam(value = "number", required = false) String number,
                                                  @RequestParam(value = "nit", required = false) String nit,
                                                  @RequestParam(value = "client_name", required = false) String clientName,
                                                  @RequestParam(value = "amount", required = false) BigDecimal amount,
                                                  @RequestParam(value = "register_type", required = false) RegisterType registerType,
                                                  @RequestParam(value = "sat_file_id", required = false) UUID satFileId,
                                                  @RequestParam(value = "status", required = false) InvoiceStatus status,
                                                  @SortDefault(sort = "createdAt", direction = DESC) final Pageable pageable,
                                                  final PagedResourcesAssembler<Sale> assembler) {

        final Page<Sale> sales = saleService.getSales(fromDate,
                toDate,
                documentNumber,
                serial,
                number,
                nit,
                clientName,
                amount,
                registerType,
                satFileId,
                status,
                pageable);

        return assembler.toModel(sales, saleResourceModelAssembler);

    }
}
