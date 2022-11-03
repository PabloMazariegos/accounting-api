package com.pmmp.controller;

import com.pmmp.assembler.SaleResourceModelAssembler;
import com.pmmp.model.Sale;
import com.pmmp.model.resource.SaleResourceModel;
import com.pmmp.service.SaleService;
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

import java.util.Date;

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
                                                  @SortDefault(sort = "createdAt", direction = DESC) final Pageable pageable,
                                                  final PagedResourcesAssembler<Sale> assembler) {

        final Page<Sale> sales = saleService.getSales(fromDate, toDate, pageable);

        return assembler.toModel(sales, saleResourceModelAssembler);

    }
}
