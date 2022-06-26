package com.pmmp.service.satfiles;

import com.pmmp.model.Sale;
import com.pmmp.model.SatFile;
import com.pmmp.model.resource.ProcessSatFileResource;
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
                .registerType(UPLOADED)
                .createdAt(convertedCreatedAt)
                .build();
    }
}
