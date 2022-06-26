package com.pmmp.service;

import com.pmmp.exception.impl.ConfigurationException;
import com.pmmp.model.TaxConfiguration;
import com.pmmp.repository.TaxConfigurationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TaxConfigurationService {
    private final String TAX_PRECISION_SLUG = "tax_precision";
    private final String TAX_ROUNDING_MODE = "tax_rounding_mode";
    private final String TAX_ISR_SLUG = "isr_tax";
    private final String TAX_IVA_SLUG = "iva_tax";

    private final TaxConfigurationRepository taxConfigurationRepository;

    public TaxConfigurationService(final TaxConfigurationRepository taxConfigurationRepository) {
        this.taxConfigurationRepository = taxConfigurationRepository;
    }

    public Integer getTaxPrecisionConfiguration() {
        final TaxConfiguration taxPrecision = taxConfigurationRepository.findBySlug(TAX_PRECISION_SLUG)
                .orElseThrow(() -> ConfigurationException.builder()
                        .message("tax precision not configured")
                        .addAdditionalInformation("slug", "tax_precision")
                        .errorMessageKey("error.tax.configuration.precision.not.found")
                        .build());

        final String taxPrecisionValue = taxPrecision.getValue();
        return Integer.parseInt(taxPrecisionValue);
    }

    public RoundingMode getTaxRoundingModeConfiguration() {
        final TaxConfiguration taxRoundingMode = taxConfigurationRepository.findBySlug(TAX_ROUNDING_MODE)
                .orElseThrow(() -> ConfigurationException.builder()
                        .message("Tax rounding mode not configured")
                        .addAdditionalInformation("slug", TAX_ROUNDING_MODE)
                        .errorMessageKey("error.tax_configurations.rounding_mode.not.found")
                        .build());


        final String roundingModeValue = taxRoundingMode.getValue();
        if (roundingModeValue.equals("DOWN")) {
            return RoundingMode.DOWN;
        }

        return RoundingMode.UP;
    }

    public BigDecimal getTaxIsrConfiguration() {
        final TaxConfiguration isrTax = taxConfigurationRepository.findBySlug(TAX_ISR_SLUG)
                .orElseThrow(() -> ConfigurationException.builder()
                        .message("isr tax not configured")
                        .addAdditionalInformation("slug", "isr_tax")
                        .errorMessageKey("error.tax.configuration.isr_tax.not.found")
                        .build());

        final String isrTaxValue = isrTax.getValue();
        return new BigDecimal(isrTaxValue);
    }

    public BigDecimal getTaxIvaConfiguration() {
        final TaxConfiguration ivaTax = taxConfigurationRepository.findBySlug(TAX_IVA_SLUG)
                .orElseThrow(() -> ConfigurationException.builder()
                        .message("iva tax not configured")
                        .addAdditionalInformation("slug", "iva_tax")
                        .errorMessageKey("error.tax.configuration.iva_tax.not.found")
                        .build());

        final String ivaTaxValue = ivaTax.getValue();
        return new BigDecimal(ivaTaxValue);
    }

    public BigDecimal getCalculatedIsr(final BigDecimal amount) {
        final BigDecimal isrTax = getTaxIsrConfiguration();
        final BigDecimal ivaTax = getTaxIvaConfiguration();
        final Integer taxPrecision = getTaxPrecisionConfiguration();
        final RoundingMode taxRoundingMode = getTaxRoundingModeConfiguration();

        final BigDecimal amountWithoutIva = amount.divide(ivaTax, taxPrecision, taxRoundingMode);
        return amountWithoutIva.multiply(isrTax).setScale(taxPrecision, taxRoundingMode);
    }

    public BigDecimal convertToBigDecimalWithTaxConfiguration(final String amountText) {
        final Integer taxPrecision = getTaxPrecisionConfiguration();
        final RoundingMode taxRoundingMode = getTaxRoundingModeConfiguration();

        return new BigDecimal(amountText).setScale(taxPrecision, taxRoundingMode);
    }
}
