package com.pmmp.service;

import com.pmmp.model.Sale;
import com.pmmp.model.SatFile;
import com.pmmp.model.resource.ProcessSatFileResource;
import com.pmmp.repository.SaleRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class ProcessSatFileService {

    private final SaleRepository salesRepository;
    private final TaxConfigurationService taxConfigurationService;

    public ProcessSatFileService(final SaleRepository salesRepository,
                                 final TaxConfigurationService taxConfigurationService) {
        this.salesRepository = salesRepository;
        this.taxConfigurationService = taxConfigurationService;
    }

    public ProcessSatFileResource processSales(final SatFile incomingSatFile) {
        final List<Sale> salesList = new ArrayList<>();
        final byte[] file = incomingSatFile.getFile();

        try {
            final InputStream fileStream = new ByteArrayInputStream(file);

            final HSSFWorkbook workbook = new HSSFWorkbook(fileStream);
            final HSSFSheet sheet = workbook.getSheetAt(0);
            final HSSFRow headerRow = sheet.getRow(0);

            if (nonNull(headerRow)) {

                final Map<String, Integer> columnsMapping = getFileColumnsMapping(headerRow);

                final int totalRows = sheet.getPhysicalNumberOfRows();
                HSSFRow dataRow;

                for (int row = 1; row <= totalRows; row++) {
                    dataRow = sheet.getRow(row);

                    if (isNull(dataRow)) {
                        continue;
                    }

                    final Sale saleFromFileRow = getSaleFromFileRow(dataRow, columnsMapping);
                    saleFromFileRow.setSatFile(incomingSatFile);
                    salesList.add(saleFromFileRow);
                }

                //TODO: validate if another sale does not have the same document_number uuid
                salesRepository.saveAll(salesList);
            }

            return ProcessSatFileResource.builder()
                    .processedRecords(salesList.size())
                    .build();

        } catch (final IOException exception) {
            throw new RuntimeException("The file could not be read", exception);
        }

    }

    public ProcessSatFileResource processPurchases(final SatFile incomingSatFile) {
        return ProcessSatFileResource.builder().build();
    }

    private Map<String, Integer> getFileColumnsMapping(final HSSFRow headerRow) {
        final short minColIdx = headerRow.getFirstCellNum();
        final short maxColIdx = headerRow.getLastCellNum();

        final Map<String, Integer> columnsMapping = new HashMap<>();
        for (int idx = minColIdx; idx < maxColIdx; idx++) {
            final HSSFCell cell = headerRow.getCell(idx);
            columnsMapping.put(cell.getStringCellValue(), cell.getColumnIndex());
        }

        return columnsMapping;
    }

    public Sale getSaleFromFileRow(final HSSFRow fileRow, final Map<String, Integer> columns) {

        final String documentNumber = getCellValue(fileRow, columns, "Número de Autorización");
        final String serial = getCellValue(fileRow, columns, "Serie");
        final String invoiceNumber = getCellValue(fileRow, columns, "Número del DTE");
        final String nit = getCellValue(fileRow, columns, "ID del receptor");
        final String clientName = getCellValue(fileRow, columns, "Nombre completo del receptor");
        final String amount = getCellValue(fileRow, columns, "Monto (Gran Total)");
        final String ivaAmount = getCellValue(fileRow, columns, "IVA (monto de este impuesto)");
        final String createdAt = getCellValue(fileRow, columns, "Fecha de emisión");

        final UUID convertedDocumentNumber = convertDocumentNumber(documentNumber);
        final BigDecimal convertedAmount = convertBigDecimalWithTaxConfiguration(amount);
        final BigDecimal convertedIvaAmount = convertBigDecimal(ivaAmount);
        final BigDecimal isrAmount = getIsrAmount(convertedIvaAmount);
        final Date convertedCreatedAt = convertCreatedAt(createdAt);

        return Sale.builder()
                .id(UUID.randomUUID())
                .documentNumber(convertedDocumentNumber)
                .serial(serial)
                .number(invoiceNumber)
                .nit(nit)
                .clientName(clientName)
                .amount(convertedAmount)
                .ivaAmount(convertedIvaAmount)
                .isrAmount(isrAmount)
                .registerType("UPLOADED")
                .createdAt(convertedCreatedAt)
                .build();
    }

    private Date convertCreatedAt(final String fileCreatedAt) {
        if (!isBlank(fileCreatedAt)) {
            final LocalDateTime localDateTime = LocalDateTime.parse(fileCreatedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    private BigDecimal getIsrAmount(final BigDecimal ivaAmount) {
        if (nonNull(ivaAmount)) {
            return taxConfigurationService.getCalculatedIsr(ivaAmount);
        }
        return null;
    }

    private BigDecimal convertBigDecimal(String fileAmount) {
        if (!isBlank(fileAmount)) {
            return new BigDecimal(fileAmount);
        }
        return null;
    }

    private BigDecimal convertBigDecimalWithTaxConfiguration(String fileAmount) {
        if (!isBlank(fileAmount)) {
            return taxConfigurationService.convertToBigDecimalWithTaxConfiguration(fileAmount);
        }
        return null;
    }

    private UUID convertDocumentNumber(final String fileDocumentNumber) {
        if (!isBlank(fileDocumentNumber)) {
            return UUID.fromString(fileDocumentNumber);
        }
        return null;
    }

    private String getCellValue(final HSSFRow fileRow,
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
