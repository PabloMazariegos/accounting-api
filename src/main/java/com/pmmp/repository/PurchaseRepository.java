package com.pmmp.repository;

import com.pmmp.model.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PurchaseRepository extends CrudRepository<Purchase, UUID> {
}
