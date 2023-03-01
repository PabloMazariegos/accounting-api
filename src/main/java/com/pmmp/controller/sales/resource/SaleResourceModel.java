package com.pmmp.controller.sales.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pmmp.model.enums.InvoiceStatus;
import com.pmmp.model.enums.RegisterType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonPropertyOrder("id")
@Relation(collectionRelation = "sales", itemRelation = "sale")
public class SaleResourceModel extends RepresentationModel<SaleResourceModel> {
    private UUID saleId;
    private UUID documentNumber;
    private String serial;
    private String number;
    private String nit;
    private String clientName;
    private BigDecimal amount;
    private BigDecimal amountWithoutIva;
    private BigDecimal ivaAmount;
    private BigDecimal isrAmount;
    private RegisterType registerType;
    private UUID satFileId;
    private InvoiceStatus status;
    private Date voidedAt;
    private Date createdAt;
}
