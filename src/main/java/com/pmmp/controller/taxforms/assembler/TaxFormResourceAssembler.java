package com.pmmp.controller.taxforms.assembler;

import com.pmmp.controller.taxforms.resource.TaxFormResourceModel;
import com.pmmp.model.TaxForm;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TaxFormResourceAssembler implements RepresentationModelAssembler<TaxForm, TaxFormResourceModel> {
    @Override
    public TaxFormResourceModel toModel(final TaxForm source) {
        return TaxFormResourceModel.builder()
                .id(source.getId())
                .accessNumber(source.getAccessNumber())
                .type(source.getType())
                .createdAt(source.getCreatedAt())
                .number(source.getNumber())
                .build();
    }
}
