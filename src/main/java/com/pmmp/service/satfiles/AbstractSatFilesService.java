package com.pmmp.service.satfiles;

import com.pmmp.service.TaxConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

public abstract class AbstractSatFilesService {

    private final TaxConfigurationService taxConfigurationService;

    protected AbstractSatFilesService(final TaxConfigurationService taxConfigurationService) {
        this.taxConfigurationService = taxConfigurationService;
    }

    protected Map<String, Integer> getFileColumnsMapping(final HSSFRow headerRow) {
        final short minColIdx = headerRow.getFirstCellNum();
        final short maxColIdx = headerRow.getLastCellNum();

        final Map<String, Integer> columnsMapping = new HashMap<>();
        for (int idx = minColIdx; idx < maxColIdx; idx++) {
            final HSSFCell cell = headerRow.getCell(idx);
            String columnName = cell.getStringCellValue();
            String normalizedColumnName = StringUtils.stripAccents(columnName);

            columnsMapping.put(normalizedColumnName.toLowerCase(), cell.getColumnIndex());
        }

        return columnsMapping;
    }

    protected Date convertCreatedAt(final String fileCreatedAt) {
        if (!isBlank(fileCreatedAt)) {
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.parse(fileCreatedAt, ISO_OFFSET_DATE_TIME);
            } catch (final Exception exception) {
                localDateTime = LocalDateTime.parse(fileCreatedAt, ISO_DATE_TIME);
            }

            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    protected BigDecimal getIsrAmount(final BigDecimal ivaAmount) {
        if (nonNull(ivaAmount)) {
            return taxConfigurationService.getCalculatedIsr(ivaAmount);
        }
        return null;
    }

    protected BigDecimal convertBigDecimal(String fileAmount) {
        if (!isBlank(fileAmount)) {
            return new BigDecimal(fileAmount);
        }
        return null;
    }

    protected BigDecimal convertBigDecimalWithTaxConfiguration(String fileAmount) {
        if (!isBlank(fileAmount)) {
            return taxConfigurationService.convertToBigDecimalWithTaxConfiguration(fileAmount);
        }
        return null;
    }

    protected UUID convertDocumentNumber(final String fileDocumentNumber) {
        if (!isBlank(fileDocumentNumber)) {
            return UUID.fromString(fileDocumentNumber);
        }
        return null;
    }

    protected String getCellValue(final HSSFRow fileRow,
                                  final Map<String, Integer> columns,
                                  final String columnName) {

        final Integer columnNumber = columns.get(columnName);
        final HSSFCell cell = fileRow.getCell(columnNumber);

        switch (cell.getCellType()) {
            case NUMERIC:
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }
}
