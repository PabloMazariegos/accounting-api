package com.pmmp.repository;

import com.pmmp.model.TaxForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface TaxFormRepository extends JpaRepository<TaxForm, UUID>, JpaSpecificationExecutor<TaxForm> {
    Optional<TaxForm> findByNumberAndAccessNumberAndType(String number, String accessNumber, String type);
}
