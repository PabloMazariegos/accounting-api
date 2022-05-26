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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sales")
public class Sale {
    @Id
    private UUID id;

    @Basic(optional = false)
    private UUID documentNumber;

    @Basic(optional = false)
    private String serial;

    @Basic(optional = false)
    private String number;

    @Basic(optional = false)
    private String nit;

    @Basic(optional = false)
    private String clientName;

    @Basic(optional = false)
    private BigDecimal amount;

    @Basic(optional = false)
    private BigDecimal ivaAmount;

    @Basic(optional = false)
    private BigDecimal isrAmount;

    @Basic(optional = false)
    private String registerType;

    @JoinColumn(name = "sat_file_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SatFile satFile;

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
