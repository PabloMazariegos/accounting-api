package com.pmmp.service;

import com.pmmp.model.Sale;
import com.pmmp.repository.SaleRepository;
import com.pmmp.repository.specification.SaleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SaleService {
    private final SaleRepository salesRepository;

    public SaleService(final SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public Page<Sale> getSales(final Date fromDate, final Date toDate, final Pageable pageable) {
        final SaleSpecification saleSpecification = new SaleSpecification(fromDate, toDate);
        return salesRepository.findAll(saleSpecification, pageable);
    }
}
