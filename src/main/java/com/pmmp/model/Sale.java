package com.pmmp.model;

import com.pmmp.model.enums.RegisterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Enumerated(STRING)
    private RegisterType registerType;

    @JoinColumn(name = "sat_file_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SatFile satFile;

    @Basic(optional = false)
    private Date createdAt;

    @Basic(optional = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
