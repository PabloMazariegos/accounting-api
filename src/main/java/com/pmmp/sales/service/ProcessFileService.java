package com.pmmp.sales.service;

import com.pmmp.model.SatFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

@Service
public class ProcessFileService {
    public static final String XLS_EXTENSION = "xls";
    public static final String XLSX_EXTENSION = "xlsx";
    private static final String FILE_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public void process(final SatFile file) {
        final String fileName = file.getName();
        final String fileExtension = FilenameUtils.getExtension(fileName);
        switch (fileExtension) {
            case XLS_EXTENSION:
                break;
            case XLSX_EXTENSION:
                break;
        }
    }
}
