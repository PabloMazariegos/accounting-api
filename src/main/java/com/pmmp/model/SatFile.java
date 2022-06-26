package com.pmmp.model;

import com.pmmp.model.enums.DocumentType;
import com.pmmp.model.enums.SatFileStatus;
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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
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
    private String fileName;

    @Basic(optional = false)
    private String extension;

    @Basic(optional = false)
    @Enumerated(STRING)
    private DocumentType type;

    @Basic(optional = false)
    private byte[] file;

    @Basic(optional = false)
    @Enumerated(STRING)
    private SatFileStatus status;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
