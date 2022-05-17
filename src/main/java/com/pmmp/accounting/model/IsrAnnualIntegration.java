package com.pmmp.accounting.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "isr_annual_integration")
public class IsrAnnualIntegration {
    @Id
    private UUID id;

    @Basic(optional = false)
    private BigDecimal isrCalculated;

    @Basic(optional = false)
    private BigDecimal retentionAmount;

    @Basic(optional = false)
    private LocalDateTime periodFrom;

    @Basic(optional = false)
    private LocalDateTime periodTo;

    @Basic(optional = false)
    private BigDecimal baseAmount;
}

