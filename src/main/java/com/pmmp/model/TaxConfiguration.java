package com.pmmp.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "taxes_configuration")
public class TaxConfiguration {
    @Id
    private UUID id;

    @Basic(optional = false)
    private String slug;

    @Basic(optional = false)
    private String value;
}
