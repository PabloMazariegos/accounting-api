package com.pmmp.repository;

import com.pmmp.model.TaxConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxConfigurationRepository extends CrudRepository<TaxConfiguration, UUID> {
    Optional<TaxConfiguration> findBySlug(String slug);
}
