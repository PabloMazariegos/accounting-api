package com.pmmp.repository;

import com.pmmp.model.SatFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SatFileRepository extends CrudRepository<SatFile, UUID> {
}
