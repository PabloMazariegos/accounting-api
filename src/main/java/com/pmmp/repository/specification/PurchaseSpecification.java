package com.pmmp.repository.specification;

import com.pmmp.model.Purchase;
import com.pmmp.model.enums.RegisterType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

public class PurchaseSpecification implements Specification<Purchase> {
    private final Date fromDate;
    private final Date toDate;
    private final String documentNumber;
    private final String documentType;
    private final String serial;
    private final String invoiceNumber;
    private final String nit;
    private final String clientName;
    private final BigDecimal amount;
    private final BigDecimal ivaAmount;
    private final RegisterType registerType;
    private final UUID satFileId;

    public PurchaseSpecification(final Date fromDate,
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
                                 final UUID satFileId) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.serial = serial;
        this.invoiceNumber = invoiceNumber;
        this.nit = nit;
        this.clientName = clientName;
        this.amount = amount;
        this.ivaAmount = ivaAmount;
        this.registerType = registerType;
        this.satFileId = satFileId;
    }

    @Override
    public Predicate toPredicate(Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (nonNull(fromDate)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
        }

        if (nonNull(toDate)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
        }

        if (nonNull(documentNumber)) {
            predicates.add(criteriaBuilder.like(root.get("id").as(String.class), "%" + documentNumber + "%"));
        }

        if (isNotBlank(documentType)) {
            predicates.add(criteriaBuilder.like(root.get("documentType"), "%" + documentType + "%"));
        }

        if (isNotBlank(serial)) {
            predicates.add(criteriaBuilder.like(root.get("serial"), "%" + serial + "%"));
        }

        if (isNotBlank(invoiceNumber)) {
            predicates.add(criteriaBuilder.like(root.get("invoiceNumber"), "%" + invoiceNumber + "%"));
        }

        if (isNotBlank(nit)) {
            predicates.add(criteriaBuilder.like(root.get("nit"), "%" + nit + "%"));
        }

        if (isNotBlank(clientName)) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("clientName")), "%" + clientName.toLowerCase() + "%"));
        }

        if (nonNull(amount)) {
            predicates.add(criteriaBuilder.like(root.get("amount").as(String.class), "%" + amount + "%"));
        }

        if (nonNull(ivaAmount)) {
            predicates.add(criteriaBuilder.like(root.get("ivaAmount").as(String.class), "%" + ivaAmount + "%"));
        }

        if (nonNull(registerType)) {
            predicates.add(criteriaBuilder.equal(root.get("registerType"), registerType));
        }

        if (nonNull(satFileId)) {
            predicates.add(criteriaBuilder.equal(root.get("satFile").get("id"), satFileId));
        }

        return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }
}
