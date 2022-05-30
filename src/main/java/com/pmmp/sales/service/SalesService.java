package com.pmmp.sales.service;

import com.pmmp.exception.impl.InternalServiceException;
import com.pmmp.model.SatFile;
import com.pmmp.repository.SatFileRepository;
import com.pmmp.sales.enums.DocumentType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;

import static org.apache.commons.codec.binary.Base64.isBase64;

@Service
public class SalesService {

    private final SatFileRepository satFileRepository;

    public SalesService(SatFileRepository satFileRepository) {
        this.satFileRepository = satFileRepository;
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

        //TODO: Crear campo status en tabla sat_files
        //TODO: Crear configuracion en redis, la idea es que al subir el archivo, el procesamiento debe ser async
        //      en esta parte se guarda el sat_file en la tabla y se publica en el canal de redis el ID generado
        //      agregar un listener que se suscriba al canal de redis y procese todos los archivos que lleguen

        satFileRepository.save(satFile);
    }
}
