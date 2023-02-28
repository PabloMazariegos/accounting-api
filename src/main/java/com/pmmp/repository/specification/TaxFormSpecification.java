package com.pmmp.repository.specification;

import com.pmmp.model.TaxForm;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Builder
public class TaxFormSpecification implements Specification<TaxForm> {
    private final Date fromDate;
    private final Date toDate;
    private final UUID id;
    private final String number;
    private final String accessNumber;
    private final String type;

    public TaxFormSpecification(final Date fromDate,
                                final Date toDate,
                                final UUID id,
                                final String number,
                                final String accessNumber,
                                final String type) {
        this.id = id;
        this.number = number;
        this.accessNumber = accessNumber;
        this.type = type;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public Predicate toPredicate(Root<TaxForm> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        if (nonNull(fromDate)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
        }

        if (nonNull(toDate)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate));
        }

        if (nonNull(id)) {
            predicates.add(criteriaBuilder.like(root.get("id").as(String.class), "%" + id + "%"));
        }

        if (isNotBlank(number)) {
            predicates.add(criteriaBuilder.like(root.get("number"), "%" + number + "%"));
        }

        if (isNotBlank(accessNumber)) {
            predicates.add(criteriaBuilder.like(root.get("accessNumber"), "%" + accessNumber + "%"));
        }

        if (isNotBlank(type)) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }

        return  criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }
}
