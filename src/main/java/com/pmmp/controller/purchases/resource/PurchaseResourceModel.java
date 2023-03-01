package com.pmmp.controller.purchases.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pmmp.model.enums.InvoiceStatus;
import com.pmmp.model.enums.RegisterType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonPropertyOrder("id")
@Relation(collectionRelation = "purchases", itemRelation = "purchase")
public class PurchaseResourceModel extends RepresentationModel<PurchaseResourceModel> {
    private UUID documentNumber;
    private String documentType;
    private String serial;
    private String invoiceNumber;
    private String nit;
    private String clientName;
    private BigDecimal amount;
    private BigDecimal ivaAmount;
    private RegisterType registerType;
    private UUID satFileId;
    private InvoiceStatus status;
    private Date voidedAt;
    private Date createdAt;
}
