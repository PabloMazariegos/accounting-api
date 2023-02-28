package com.pmmp.controller.sales.service;

import com.pmmp.model.Sale;
import com.pmmp.model.enums.RegisterType;
import com.pmmp.repository.SaleRepository;
import com.pmmp.repository.specification.SaleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class SaleService {
    private final SaleRepository salesRepository;

    public SaleService(final SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Page<Sale> getSales(final Date fromDate,
                               final Date toDate,
                               final String documentNumber,
                               final String serial,
                               final String number,
                               final String nit,
                               final String clientName,
                               final BigDecimal amount,
                               final RegisterType registerType,
                               final UUID satFileId,
                               final Pageable pageable) {

        final SaleSpecification saleSpecification = new SaleSpecification(fromDate,
                toDate,
                documentNumber,
                serial,
                number,
                nit,
                clientName,
                amount,
                registerType,
                satFileId);

        return salesRepository.findAll(saleSpecification, pageable);
    }
}
