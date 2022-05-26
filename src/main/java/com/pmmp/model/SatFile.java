package com.pmmp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private byte[] file;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date uploadedAt;

    @CreatedBy
    private String uploadedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
