package com.pmmp.controller.purchases.assembler;

import com.pmmp.model.Purchase;
import com.pmmp.controller.purchases.resource.PurchaseResourceModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PurchaseResourceModelAssembler implements RepresentationModelAssembler<Purchase, PurchaseResourceModel> {

    @Override
    public PurchaseResourceModel toModel(final Purchase source) {
        return PurchaseResourceModel.builder()
                .documentNumber(source.getId())
                .documentType(source.getDocumentType())
                .serial(source.getSerial())
                .invoiceNumber(source.getInvoiceNumber())
                .nit(source.getNit())
                .clientName(source.getClientName())
                .amount(source.getAmount())
                .ivaAmount(source.getIvaAmount())
                .registerType(source.getRegisterType())
                .satFileId(source.getSatFile().getId())
                .status(source.getStatus())
                .voidedAt(source.getVoidedAt())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
