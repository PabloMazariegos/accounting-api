package com.pmmp.service;

import com.pmmp.model.Sale;
import com.pmmp.model.SatFile;
import com.pmmp.model.resource.ProcessSatFileResource;
import com.pmmp.repository.SaleRepository;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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

import static java.math.RoundingMode.UP;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class ProcessSatFileService {
    private static final String FILE_DATE_FORMAT = "yyyy-mm-dd HH:mm:ss";

    private final SaleRepository salesRepository;

    public ProcessSatFileService(final SaleRepository salesRepository) {
        this.salesRepository = salesRepository;
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
                final short minColIdx = headerRow.getFirstCellNum();
                final short maxColIdx = headerRow.getLastCellNum();

                //TODO: Wrap in a separate function
                final Map<String, Integer> map = new HashMap<>();
                for (int idx = minColIdx; idx < maxColIdx; idx++) {
                    final HSSFCell cell = headerRow.getCell(idx);
                    map.put(cell.getStringCellValue(), cell.getColumnIndex());
                }

                final int totalRows = sheet.getPhysicalNumberOfRows();
                HSSFRow dataRow;

                for (int row = 1; row <= totalRows; row++) {
                    dataRow = sheet.getRow(row);

                    if (isNull(dataRow)) {
                        continue;
                    }

                    //TODO: Wrap in a separate function that return the converted type values
                    final String documentNumber = getCellValue(dataRow.getCell(map.get("Número de Autorización")));
                    final String serial = getCellValue(dataRow.getCell(map.get("Serie")));
                    final String invoiceNumber = getCellValue(dataRow.getCell(map.get("Número del DTE")));
                    final String nit = getCellValue(dataRow.getCell(map.get("ID del receptor")));
                    final String clientName = getCellValue(dataRow.getCell(map.get("Nombre completo del receptor")));
                    final String amount = getCellValue(dataRow.getCell(map.get("Monto (Gran Total)")));
                    final String ivaAmount = getCellValue(dataRow.getCell(map.get("IVA (monto de este impuesto)")));
                    final String createdAt = getCellValue(dataRow.getCell(map.get("Fecha de emisión")));

                    UUID convertedDocumentNumber = null;
                    BigDecimal convertedAmount = null;
                    BigDecimal convertedIva = null;
                    Date convertedDate = null;
                    BigDecimal isrAmount = new BigDecimal("0.00");

                    if (!isBlank(documentNumber)) {
                        convertedDocumentNumber = UUID.fromString(documentNumber);
                    }

                    if (!isBlank(amount)) {

                        convertedAmount = new BigDecimal(amount).setScale(2, UP);

                        //TODO: Configure ISR_TAX, IVA_TAX & TAX_PRECISION in table taxes_configurations
                        //TODO: Create a service for calculate the taxes
                        final BigDecimal isrTax = new BigDecimal("0.05");
                        final BigDecimal ivaTax = new BigDecimal("1.12");

                        final BigDecimal amountWithoutIva = convertedAmount.divide(ivaTax, 2, UP);
                        isrAmount = amountWithoutIva.multiply(isrTax).setScale(2, UP);
                    }

                    if (!isBlank(ivaAmount)) {
                        convertedIva = new BigDecimal(ivaAmount);
                    }

                    if (!isBlank(createdAt)) {
                        final LocalDateTime localDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        convertedDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    }

                    final Sale newSale = Sale.builder()
                            .id(UUID.randomUUID())
                            .documentNumber(convertedDocumentNumber)
                            .serial(serial)
                            .number(invoiceNumber)
                            .nit(nit)
                            .clientName(clientName)
                            .amount(convertedAmount)
                            .ivaAmount(convertedIva)
                            .isrAmount(isrAmount)
                            .registerType("UPLOADED")
                            .satFile(incomingSatFile)
                            .createdAt(convertedDate)
                            .build();

                    salesList.add(newSale);
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

    private String getCellValue(final Cell cell) {
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
