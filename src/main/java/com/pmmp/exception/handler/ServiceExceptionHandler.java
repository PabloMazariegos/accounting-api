package com.pmmp.exception.handler;

import com.pmmp.exception.AbstractServiceException;
import com.pmmp.exception.ErrorMessage;
import com.pmmp.exception.impl.*;
import com.pmmp.exception.resource.ErrorItemResource;
import com.pmmp.exception.resource.ErrorMessageResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String RUNTIME_EXCEPTION_CODE = "001-1000";
    private static final String RUNTIME_EXCEPTION_MESSAGE_CODE = "global.error.unknown";

    private final MessageSource messageSource;


    public ServiceExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorMessageResource> handleRuntimeException(final RuntimeException exception) {
        log.error("Unhandled RuntimeException has been thrown." + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(RUNTIME_EXCEPTION_CODE, RUNTIME_EXCEPTION_MESSAGE_CODE, null);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(errorMessageResource);
    }

    @ExceptionHandler({ConfigurationException.class, InternalServiceException.class, InternalInconsistencyException.class})
    public ResponseEntity<ErrorMessageResource> handleInternalServerError(final AbstractServiceException exception) {
        log.error("Internal server error has been thrown." + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(errorMessageResource);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorMessageResource> handleBadRequestException(final AbstractServiceException exception) {
        log.debug("BadRequestException has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(errorMessageResource);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessageResource> handleResourceNotFoundExceptions(final ResourceNotFoundException exception) {
        log.debug("ResourceNotFoundException has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);
        return ResponseEntity
                .status(NOT_FOUND)
                .body(errorMessageResource);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                                             final HttpHeaders headers,
                                                                             final HttpStatus status,
                                                                             final WebRequest request){
        log.debug("MethodArgumentNotValidException has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(errorMessageResource);

    }

    private ErrorMessageResource getErrorMessageResource(final String code, final String messageCode, final Object[] args) {
        final String message = messageSource.getMessage(messageCode, args, messageCode, getLocale());

        return ErrorMessageResource.builder()
                .code(code)
                .message(message)
                .build();
    }

    private ErrorMessageResource getErrorMessageResource(final String code,
                                                         final String messageCode,
                                                         final Object[] args,
                                                         final BindingResult bindingResult) {
        final String message = messageSource.getMessage(messageCode, args, messageCode, getLocale());

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ErrorItemResource> itemErrors = fieldErrors.stream()
                .map(fieldError -> ErrorItemResource.builder()
                        .path(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ErrorMessageResource.builder()
                .code(code)
                .message(message)
                .errors(itemErrors)
                .build();
    }

    private ErrorMessageResource getErrorMessageResource(final AbstractServiceException abstractServiceException) {
        final ErrorMessage errorMessage = abstractServiceException.getErrorMessage();
        return getErrorMessageResource(abstractServiceException.getCode(), errorMessage.getCode(), errorMessage.getArgs());
    }

    private ErrorMessageResource getErrorMessageResource(final MethodArgumentNotValidException methodArgumentNotValidException) {
        final BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        return getErrorMessageResource(RUNTIME_EXCEPTION_CODE, RUNTIME_EXCEPTION_MESSAGE_CODE, null, bindingResult);
    }
}
