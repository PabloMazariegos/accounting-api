package com.pmmp.repository.specification;

import com.pmmp.model.Sale;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

public class SaleSpecification implements Specification<Sale> {
    private final Date fromDate;
    private final Date toDate;

    public SaleSpecification(final Date fromDate, final Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
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

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
