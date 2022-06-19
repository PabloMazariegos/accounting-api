package com.pmmp.listener.service;

import com.pmmp.exception.impl.InternalServiceException;
import com.pmmp.exception.impl.ResourceNotFoundException;
import com.pmmp.listener.model.UploadSatFileMessage;
import com.pmmp.model.SatFile;
import com.pmmp.model.enums.DocumentType;
import com.pmmp.model.resource.ProcessSatFileResource;
import com.pmmp.repository.SatFileRepository;
import com.pmmp.service.ProcessSatFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pmmp.model.enums.SatFileStatus.COMPLETED;
import static com.pmmp.model.enums.SatFileStatus.IN_PROGRESS;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class UploadSatFileMessageService {
    private final SatFileRepository satFileRepository;
    private final ProcessSatFileService processSatFileService;

    public UploadSatFileMessageService(final SatFileRepository satFileRepository,
                                       final ProcessSatFileService processSatFileService) {
        this.satFileRepository = satFileRepository;
        this.processSatFileService = processSatFileService;
    }

    public void process(final UploadSatFileMessage uploadSatFileMessage) {
        log.info("Message with id {} receive successfully", uploadSatFileMessage.getSatFileId());

        final UUID incomingSatFileId = uploadSatFileMessage.getSatFileId();

        final SatFile incomingSatFile =
                satFileRepository.findById(incomingSatFileId)
                        .orElseThrow(() -> ResourceNotFoundException.builder()
                                .message("The uploaded file was not found")
                                .addAdditionalInformation("satFileId", incomingSatFileId)
                                .errorMessageKey("error.files.upload.not.found")
                                .build()
                        );

        incomingSatFile.setStatus(IN_PROGRESS);
        satFileRepository.save(incomingSatFile);

        final DocumentType incomingSatFileType = incomingSatFile.getType();
        ProcessSatFileResource fileProcessComplete;

        switch (incomingSatFileType) {
            case SALES_SAT_FILE:
                fileProcessComplete = processSatFileService.processSales(incomingSatFile);
                break;
            case PURCHASES_SAT_FILE:
                fileProcessComplete = processSatFileService.processPurchases(incomingSatFile);
                break;
            default:
                throw InternalServiceException.builder()
                        .message("File type not supported yet")
                        .addAdditionalInformation("fileExtension", incomingSatFileType.name())
                        .errorMessageKey("error.files.type.not.supported")
                        .build();
        }

        if (nonNull(fileProcessComplete)) {
            incomingSatFile.setStatus(COMPLETED);
            satFileRepository.save(incomingSatFile);
        }
    }
}
