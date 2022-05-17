package com.pmmp.accounting.model;

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
import java.util.Date;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sat_files")
public class SatFile {
    @Id
    private UUID id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String type;

    @Basic(optional = false)
    private Byte[] file;

    @Basic(optional = false)
    @Column(updatable = false)
    @CreatedDate
    private Date uploadedAt;

    @Basic(optional = false)
    @Column(updatable = false)
    @CreatedBy
    private String uploadedBy;

    @LastModifiedDate
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
