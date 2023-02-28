package com.pmmp.controller.taxforms.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CreateTaxFormRequest {
    @NotBlank(message = "{tax-forms.request.error.number.not-blank}")
    private final String number;

    @NotBlank(message = "{tax-forms.request.error.accessNumber.not-blank}")
    private final String accessNumber;

    @NotBlank(message = "{tax-forms.request.error.type.not-blank}")
    private final String type;
}
