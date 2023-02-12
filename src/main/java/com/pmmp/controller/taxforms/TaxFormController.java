package com.pmmp.controller.taxforms;

import com.pmmp.controller.taxforms.assembler.TaxFormResourceAssembler;
import com.pmmp.controller.taxforms.resource.TaxFormResourceModel;
import com.pmmp.controller.taxforms.service.TaxFormService;
import com.pmmp.model.TaxForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/v1/tax-forms")
public class TaxFormController {
    private final TaxFormService taxFormService;
    private final TaxFormResourceAssembler taxFormResourceAssembler;

    public TaxFormController(final TaxFormService taxFormService,
                             final TaxFormResourceAssembler taxFormResourceAssembler) {
        this.taxFormService = taxFormService;
        this.taxFormResourceAssembler = taxFormResourceAssembler;
    }

    @GetMapping
    @ResponseStatus(OK)
    public PagedModel<TaxFormResourceModel> getTaxForms(@RequestParam(value = "from_date") @DateTimeFormat(iso = DATE) Date fromDate,
                                                        @RequestParam(value = "to_date") @DateTimeFormat(iso = DATE) Date toDate,
                                                        @RequestParam(value = "number", required = false) String number,
                                                        @RequestParam(value = "access_number", required = false) String accessNumber,
                                                        @RequestParam(value = "type", required = false) String type,
                                                        @RequestParam(value = "id", required = false) UUID id,
                                                        @SortDefault(sort = "createdAt", direction = DESC) final Pageable pageable,
                                                        final PagedResourcesAssembler<TaxForm> assembler){
        Page<TaxForm> taxForms = taxFormService.getTaxForms(fromDate, toDate, number, accessNumber, type, id, pageable);
        return assembler.toModel(taxForms, taxFormResourceAssembler);
    }
}
