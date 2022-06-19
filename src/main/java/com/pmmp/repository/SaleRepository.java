package com.pmmp.repository;

import com.pmmp.model.Sale;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SaleRepository extends CrudRepository<Sale, UUID> {
}
