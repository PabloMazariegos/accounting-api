package com.pmmp.controller.taxforms.resource;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@JsonPropertyOrder("id")
@Relation(collectionRelation = "tax_forms", itemRelation = "tax_form")
public class TaxFormResourceModel extends RepresentationModel<TaxFormResourceModel> {
    private UUID id;
    private Long number;
    private Long accessNumber;
    private String type;
    private Date createdAt;
}
