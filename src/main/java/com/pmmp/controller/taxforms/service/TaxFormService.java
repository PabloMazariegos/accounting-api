package com.pmmp.controller.taxforms.service;

import com.pmmp.model.TaxForm;
import com.pmmp.repository.TaxFormRepository;
import com.pmmp.repository.specification.TaxFormSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TaxFormService {
    private final TaxFormRepository taxFormRepository;

    public TaxFormService(final TaxFormRepository taxFormRepository) {
        this.taxFormRepository = taxFormRepository;
    }

    public Page<TaxForm> getTaxForms(final Date fromDate,
                                     final Date toDate,
                                     final Long number,
                                     final Long accessNumber,
                                     final String type,
                                     final UUID id,
                                     final Pageable pageable) {

        final TaxFormSpecification taxFormSpecification = TaxFormSpecification.builder()
                .fromDate(fromDate)
                .toDate(toDate)
                .number(number)
                .accessNumber(accessNumber)
                .type(type)
                .id(id)
                .build();

        return taxFormRepository.findAll(taxFormSpecification, pageable);
    }
}
