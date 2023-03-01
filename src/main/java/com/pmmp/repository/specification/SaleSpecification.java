package com.pmmp.repository.specification;

import com.pmmp.model.Sale;
import com.pmmp.model.enums.InvoiceStatus;
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

public class SaleSpecification implements Specification<Sale> {
    private final Date fromDate;
    private final Date toDate;
    private final String documentNumber;
    private final String serial;
    private final String number;
    private final String nit;
    private final String clientName;
    private final BigDecimal amount;
    private final RegisterType registerType;
    private final UUID satFileId;
    private final InvoiceStatus status;

    public SaleSpecification(final Date fromDate,
                             final Date toDate,
                             final String documentNumber,
                             final String serial,
                             final String number,
                             final String nit,
                             final String clientName,
                             final BigDecimal amount,
                             final RegisterType registerType,
                             final UUID satFileId,
                             final InvoiceStatus status) {

        this.fromDate = fromDate;
        this.toDate = toDate;
        this.documentNumber = documentNumber;
        this.serial = serial;
        this.number = number;
        this.nit = nit;
        this.clientName = clientName;
        this.amount = amount;
        this.registerType = registerType;
        this.satFileId = satFileId;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<Sale> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (nonNull(fromDate)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
        }

        if (nonNull(toDate)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
        }

        if (isNotBlank(documentNumber)) {
            predicates.add(criteriaBuilder.like(root.get("documentNumber").as(String.class), "%" + documentNumber + "%"));
        }

        if (isNotBlank(serial)) {
            predicates.add(criteriaBuilder.like(root.get("serial"), "%" + serial + "%"));
        }

        if (isNotBlank(number)) {
            predicates.add(criteriaBuilder.like(root.get("number"), "%" + number + "%"));
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

        if (nonNull(registerType)) {
            predicates.add(criteriaBuilder.equal(root.get("registerType"), registerType));
        }

        if (nonNull(satFileId)) {
            predicates.add(criteriaBuilder.equal(root.get("satFile").get("id"), satFileId));
        }

        if(nonNull(status)){
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
