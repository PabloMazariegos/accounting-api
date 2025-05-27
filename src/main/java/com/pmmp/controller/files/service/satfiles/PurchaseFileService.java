package com.pmmp.controller.files.service.satfiles;

import com.pmmp.model.Purchase;
import com.pmmp.model.SatFile;
import com.pmmp.controller.files.resource.ProcessSatFileResource;
import com.pmmp.model.enums.InvoiceStatus;
import com.pmmp.repository.PurchaseRepository;
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
public class PurchaseFileService extends AbstractSatFilesService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseFileService(final PurchaseRepository purchaseRepository,
                               final TaxConfigurationService taxConfigurationService) {
        super(taxConfigurationService);
        this.purchaseRepository = purchaseRepository;
    }

    public ProcessSatFileResource process(final SatFile incomingSatFile) {
        final List<Purchase> purchaseList = new ArrayList<>();
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

                    final Purchase purchaseFromFileRow = getPurchaseFromFileRow(dataRow, columnsMapping);
                    purchaseFromFileRow.setSatFile(incomingSatFile);
                    purchaseList.add(purchaseFromFileRow);
                }

                purchaseRepository.saveAll(purchaseList);
            }

            return ProcessSatFileResource.builder()
                    .processedRecords(purchaseList.size())
                    .build();

        } catch (final IOException exception) {
            throw new RuntimeException("The file could not be read", exception);
        }
    }

    public Purchase getPurchaseFromFileRow(final HSSFRow fileRow, final Map<String, Integer> columns) {
        final String documentType = getCellValue(fileRow, columns, "tipo de dte (nombre)");
        final String serial = getCellValue(fileRow, columns, "serie");
        final String invoiceNumber = getCellValue(fileRow, columns, "numero del dte");
        final String nit = getCellValue(fileRow, columns, "nit del emisor");
        final String clientName = getCellValue(fileRow, columns, "nombre completo del emisor");
        final String amount = getCellValue(fileRow, columns, "gran total (moneda original)");
        final String ivaAmount = getCellValue(fileRow, columns, "iva (monto de este impuesto)");
        final String createdAt = getCellValue(fileRow, columns, "fecha de emision");
        final String status = getCellValue(fileRow, columns, "estado");
        final String voidedAt = getCellValue(fileRow, columns, "fecha de anulacion");

        final BigDecimal convertedAmount = convertBigDecimalWithTaxConfiguration(amount);
        final BigDecimal convertedIvaAmount = convertBigDecimal(ivaAmount);
        final Date convertedCreatedAt = convertToDateWithoutTimeZone(createdAt);
        final BigDecimal amountWithoutIva = convertedAmount.subtract(convertedIvaAmount);
        final InvoiceStatus invoiceStatus = convertToInvoiceStatus(status);
        final Date convertedVoidedAt = convertToDateWithoutTimeZone(voidedAt);

        return Purchase.builder()
                .id(UUID.randomUUID())
                .documentType(documentType)
                .serial(serial)
                .invoiceNumber(invoiceNumber)
                .nit(nit)
                .clientName(clientName)
                .amount(convertedAmount)
                .ivaAmount(convertedIvaAmount)
                .amountWithoutIva(amountWithoutIva)
                .registerType(UPLOADED)
                .status(invoiceStatus)
                .voidedAt(convertedVoidedAt)
                .createdAt(convertedCreatedAt)
                .build();
    }
}
