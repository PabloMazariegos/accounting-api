package com.pmmp.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tax_forms")
public class TaxForm {
    @Id
    private UUID id;

    @Basic(optional = false)
    private Long number;

    @Basic(optional = false)
    private Long accessNumber;

    @Basic(optional = false)
    private String type;

    private BigDecimal amount;

    @Basic(optional = false)
    @Column(updatable = false)
    @CreatedDate
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
