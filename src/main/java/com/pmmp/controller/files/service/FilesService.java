package com.pmmp.controller.files.service;

import com.pmmp.exception.impl.InternalServiceException;
import com.pmmp.model.SatFile;
import com.pmmp.model.enums.DocumentType;
import com.pmmp.controller.files.request.UploadSatFileRequestMessage;
import com.pmmp.repository.SatFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;

import static com.pmmp.model.enums.SatFileStatus.PENDING;
import static org.apache.tomcat.util.codec.binary.Base64.isBase64;

@Service
public class FilesService {

    public static final String UPLOAD_SAT_FILE = "UPLOAD_SAT_SALES_FILE";

    private final SatFileRepository satFileRepository;
    private final UploadSatFileMessageService uploadSatFileMessageService;

    public FilesService(final SatFileRepository satFileRepository,
                        final UploadSatFileMessageService uploadSatFileMessageService) {

        this.satFileRepository = satFileRepository;
        this.uploadSatFileMessageService = uploadSatFileMessageService;
    }

    @Transactional
    public void processFile(final String file, final String fileName, final DocumentType documentTypeSlug) {

        final boolean isBase64StringValid = isBase64(file);

        if (!isBase64StringValid) {
            throw InternalServiceException.builder()
                    .message("The string with the base64 value of the file is invalid")
                    .errorMessageKey("error.files.invalid.base64")
                    .build();
        }

        final String fileExtension = FilenameUtils.getExtension(fileName);
        final byte[] fileContent = Base64.getDecoder().decode(file);

        final SatFile satFile = SatFile.builder()
                .id(UUID.randomUUID())
                .fileName(fileName)
                .extension(fileExtension)
                .type(documentTypeSlug)
                .file(fileContent)
                .status(PENDING)
                .build();

        satFileRepository.save(satFile);

        final UploadSatFileRequestMessage uploadSatFileRequestMessage = UploadSatFileRequestMessage.builder()
                .uuid(UUID.randomUUID())
                .action(UPLOAD_SAT_FILE)
                .satFileId(satFile.getId())
                .build();

        uploadSatFileMessageService.process(uploadSatFileRequestMessage);
    }
}
