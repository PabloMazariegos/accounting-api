package com.pmmp.controller.sales.assembler;

import com.pmmp.model.Sale;
import com.pmmp.controller.sales.resource.SaleResourceModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SaleResourceModelAssembler implements RepresentationModelAssembler<Sale, SaleResourceModel> {

    @Override
    public SaleResourceModel toModel(final Sale source) {
        return SaleResourceModel.builder()
                .saleId(source.getId())
                .documentNumber(source.getDocumentNumber())
                .serial(source.getSerial())
                .number(source.getNumber())
                .nit(source.getNit())
                .clientName(source.getClientName())
                .amount(source.getAmount())
                .ivaAmount(source.getIvaAmount())
                .isrAmount(source.getIsrAmount())
                .registerType(source.getRegisterType())
                .satFileId(source.getSatFile().getId())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
