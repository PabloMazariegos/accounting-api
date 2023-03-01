package com.pmmp.controller.files.service.satfiles;

import com.pmmp.model.Sale;
import com.pmmp.model.SatFile;
import com.pmmp.controller.files.resource.ProcessSatFileResource;
import com.pmmp.model.enums.InvoiceStatus;
import com.pmmp.repository.SaleRepository;
import com.pmmp.service.TaxConfigurationService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.pmmp.model.enums.RegisterType.UPLOADED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class SalesFileService extends AbstractSatFilesService {

    private final SaleRepository salesRepository;

    public SalesFileService(final SaleRepository salesRepository,
                            final TaxConfigurationService taxConfigurationService) {
        super(taxConfigurationService);
        this.salesRepository = salesRepository;
    }

    public ProcessSatFileResource process(final SatFile incomingSatFile) {
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

                salesRepository.saveAll(salesList);
            }

            return ProcessSatFileResource.builder()
                    .processedRecords(salesList.size())
                    .build();

        } catch (final IOException exception) {
            throw new RuntimeException("The file could not be read", exception);
        }

    }

    public Sale getSaleFromFileRow(final HSSFRow fileRow, final Map<String, Integer> columns) {

        final String documentNumber = getCellValue(fileRow, columns, "numero de autorizacion");
        final String serial = getCellValue(fileRow, columns, "serie");
        final String invoiceNumber = getCellValue(fileRow, columns, "numero del dte");
        final String nit = getCellValue(fileRow, columns, "id del receptor");
        final String clientName = getCellValue(fileRow, columns, "nombre completo del receptor");
        final String amount = getCellValue(fileRow, columns, "monto (gran total)");
        final String ivaAmount = getCellValue(fileRow, columns, "iva (monto de este impuesto)");
        final String createdAt = getCellValue(fileRow, columns, "fecha de emision");
        final String status = getCellValue(fileRow, columns, "estado");
        final String voidedAt = getCellValue(fileRow, columns, "fecha de anulacion");

        final UUID convertedDocumentNumber = convertDocumentNumber(documentNumber);
        final BigDecimal convertedAmount = convertBigDecimalWithTaxConfiguration(amount);
        final BigDecimal convertedIvaAmount = convertBigDecimal(ivaAmount);
        final BigDecimal isrAmount = getIsrAmount(convertedIvaAmount);
        final Date convertedCreatedAt = convertToDateWithoutTimeZone(createdAt);
        final BigDecimal amountWithoutIva = convertedAmount.subtract(convertedIvaAmount);
        final Date convertedVoidedAt = convertToDateWithoutTimeZone(voidedAt);
        final InvoiceStatus invoiceStatus = convertToInvoiceStatus(status);

        return Sale.builder()
                .id(UUID.randomUUID())
                .documentNumber(convertedDocumentNumber)
                .serial(serial)
                .number(invoiceNumber)
                .nit(nit)
                .clientName(clientName)
                .amount(convertedAmount)
                .ivaAmount(convertedIvaAmount)
                .amountWithoutIva(amountWithoutIva)
                .isrAmount(isrAmount)
                .registerType(UPLOADED)
                .status(invoiceStatus)
                .voidedAt(convertedVoidedAt)
                .createdAt(convertedCreatedAt)
                .build();
    }
}
