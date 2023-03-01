package com.pmmp.controller.taxforms.service;

import com.pmmp.controller.taxforms.request.CreateTaxFormRequest;
import com.pmmp.exception.impl.InternalInconsistencyException;
import com.pmmp.model.TaxForm;
import com.pmmp.repository.TaxFormRepository;
import com.pmmp.repository.specification.TaxFormSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaxFormService {
    private final TaxFormRepository taxFormRepository;

    public TaxFormService(final TaxFormRepository taxFormRepository) {
        this.taxFormRepository = taxFormRepository;
    }

    public Page<TaxForm> getTaxForms(final LocalDate fromDate,
                                     final LocalDate toDate,
                                     final String number,
                                     final String accessNumber,
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

    public void createTaxForm(final CreateTaxFormRequest createTaxFormRequest) {
        final Optional<TaxForm> optionalTaxForm = validateIfTaxFormExists(createTaxFormRequest);

        if(optionalTaxForm.isPresent()){
            final TaxForm existentTaxForm = optionalTaxForm.get();

            throw InternalInconsistencyException.builder()
                    .message("We had a tax form stored with the same values of the request.")
                    .errorMessageKey("tax-form.error.already-exists")
                    .addAdditionalInformation("number", existentTaxForm.getNumber())
                    .addAdditionalInformation("accessNumber", existentTaxForm.getAccessNumber())
                    .addAdditionalInformation("type", existentTaxForm.getType())
                    .build();
        }

        final TaxForm taxForm = TaxForm.builder()
                .id(UUID.randomUUID())
                .number(createTaxFormRequest.getNumber())
                .accessNumber(createTaxFormRequest.getAccessNumber())
                .type(createTaxFormRequest.getType())
                .filedAt(createTaxFormRequest.getFiledAt())
                .build();

        taxFormRepository.save(taxForm);
    }

    private Optional<TaxForm> validateIfTaxFormExists(final CreateTaxFormRequest createTaxFormRequest) {
        String number = createTaxFormRequest.getNumber();
        String accessNumber = createTaxFormRequest.getAccessNumber();
        String type = createTaxFormRequest.getType();

        return taxFormRepository.findByNumberAndAccessNumberAndType(number, accessNumber, type);
    }
}
