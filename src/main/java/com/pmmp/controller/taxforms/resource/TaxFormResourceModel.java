package com.pmmp.controller.taxforms.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonPropertyOrder("id")
@Relation(collectionRelation = "tax_forms", itemRelation = "tax_form")
public class TaxFormResourceModel extends RepresentationModel<TaxFormResourceModel> {
    private UUID id;
    private String number;
    private String accessNumber;
    private String type;
    private LocalDate filedAt;
    private Date createdAt;
}
