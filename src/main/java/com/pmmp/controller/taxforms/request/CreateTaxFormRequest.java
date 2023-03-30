package com.pmmp.controller.taxforms.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class CreateTaxFormRequest {
    @NotBlank(message = "{tax-forms.request.error.number.not-blank}")
    private String number;

    @NotBlank(message = "{tax-forms.request.error.accessNumber.not-blank}")
    private String accessNumber;

    @NotBlank(message = "{tax-forms.request.error.type.not-blank}")
    private String type;

    @NotNull(message = "{tax-forms.request.error.filed-at.not-blank}")
    private LocalDate filedAt;
}
