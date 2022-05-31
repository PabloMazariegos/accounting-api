package com.pmmp.sales.service;

import com.pmmp.exception.impl.ConfigurationException;
import com.pmmp.exception.impl.InternalServiceException;
import com.pmmp.listener.model.UploadSatFileRequestMessage;
import com.pmmp.model.SatFile;
import com.pmmp.repository.SatFileRepository;
import com.pmmp.sales.enums.DocumentType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.core.DestinationResolutionException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;

import static com.pmmp.config.rabbitmq.RabbitMQConfig.EXCHANGE_NAME;
import static com.pmmp.config.rabbitmq.RabbitMQConfig.QUEUE_NAME;
import static com.pmmp.config.rabbitmq.RabbitMQConfig.ROUTING_KEY;
import static org.apache.tomcat.util.codec.binary.Base64.isBase64;

@Service
public class SalesService {

    private final SatFileRepository satFileRepository;
    private final RabbitTemplate rabbitTemplate;

    private final String applicationName;

    public SalesService(final SatFileRepository satFileRepository,
                        final RabbitTemplate rabbitTemplate,
                        @Value("accounting-api") final String applicationName) {

        this.satFileRepository = satFileRepository;
        this.rabbitTemplate = rabbitTemplate;
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

        final byte[] fileContent = Base64.getDecoder().decode(file);

        final SatFile satFile = SatFile.builder()
                .id(UUID.randomUUID())
                .name(fileName)
                .type(documentTypeSlug.name())
                .file(fileContent)
                .build();

        satFileRepository.save(satFile);

        final UploadSatFileRequestMessage uploadSatFileRequestMessage = UploadSatFileRequestMessage.builder()
                .uuid(UUID.randomUUID())
                .source(applicationName)
                .satFileId(satFile.getId())
                .build();

        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, uploadSatFileRequestMessage);

        } catch (final DestinationResolutionException exception) {
            throw ConfigurationException.builder()
                    .message("The queue for accounting-api does not exists.")
                    .addAdditionalInformation("accountingQueueName", QUEUE_NAME)
                    .build();
        }

        //TODO: Crear campo status en tabla sat_files
        //TODO: Crear configuracion en redis, la idea es que al subir el archivo, el procesamiento debe ser async
        //      en esta parte se guarda el sat_file en la tabla y se publica en el canal de redis el ID generado
        //      agregar un listener que se suscriba al canal de redis y procese todos los archivos que lleguen


    }
}
