package com.pmmp.service;

import com.pmmp.model.Purchase;
import com.pmmp.model.enums.RegisterType;
import com.pmmp.repository.PurchaseRepository;
import com.pmmp.repository.specification.PurchaseSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(final PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Page<Purchase> getPurchases(final Date fromDate,
                                       final Date toDate,
                                       final String documentNumber,
                                       final String documentType,
                                       final String serial,
                                       final String invoiceNumber,
                                       final String nit,
                                       final String clientName,
                                       final BigDecimal amount,
                                       final BigDecimal ivaAmount,
                                       final RegisterType registerType,
                                       final UUID satFileId,
                                       final Pageable pageable) {

        final PurchaseSpecification purchaseSpecification = new PurchaseSpecification(fromDate,
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
                satFileId);

        return purchaseRepository.findAll(purchaseSpecification, pageable);
    }
}
