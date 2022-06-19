package com.pmmp.service;

import com.pmmp.config.rabbitmq.RabbitMQProperties;
import com.pmmp.exception.impl.ConfigurationException;
import com.pmmp.exception.impl.InternalServiceException;
import com.pmmp.listener.model.UploadSatFileRequestMessage;
import com.pmmp.listener.service.QueueMessageService;
import com.pmmp.model.SatFile;
import com.pmmp.model.enums.DocumentType;
import com.pmmp.repository.SatFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;

import static com.pmmp.listener.model.QueueMessage.UPLOAD_SAT_FILE;
import static com.pmmp.model.enums.SatFileStatus.PENDING;
import static org.apache.tomcat.util.codec.binary.Base64.isBase64;

@Service
public class SalesService {

    private final SatFileRepository satFileRepository;
    private final QueueMessageService queueMessageService;
    private final RabbitMQProperties rabbitProperties;

    private final String applicationName;

    public SalesService(final SatFileRepository satFileRepository,
                        final QueueMessageService queueMessageService,
                        RabbitMQProperties rabbitMQProperties, @Value("accounting-api") final String applicationName) {

        this.satFileRepository = satFileRepository;
        this.queueMessageService = queueMessageService;
        this.rabbitProperties = rabbitMQProperties;
        this.applicationName = applicationName;
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
                .source(applicationName)
                .satFileId(satFile.getId())
                .build();

        try {
            queueMessageService.convertAndSend(
                    rabbitProperties.getExchange().getRetry(),
                    rabbitProperties.getRouting().getRetry(),
                    uploadSatFileRequestMessage);

        } catch (final DestinationResolutionException exception) {
            throw ConfigurationException.builder()
                    .message("The queue for accounting-api does not exists.")
                    .addAdditionalInformation("accountingQueueName", rabbitProperties.getQueue().getRetry())
                    .build();
        }
    }
}
