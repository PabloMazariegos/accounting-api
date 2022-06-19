package com.pmmp.model;

import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "purchases")
public class Purchase {
    @Id
    private UUID id;

    @Basic(optional = false)
    private String documentType;

    @Basic(optional = false)
    private String serial;

    @Basic(optional = false)
    private String invoiceNumber;

    @Basic(optional = false)
    private String nit;

    @Basic(optional = false)
    private String clientName;

    @Basic(optional = false)
    private BigDecimal amount;

    @Basic(optional = false)
    private BigDecimal ivaAmount;

    @Basic(optional = false)
    private String registerType;

    @JoinColumn(name = "sat_file_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SatFile satFile;

    @Basic(optional = false)
    private Date createdAt;

    @Basic(optional = false)
    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
