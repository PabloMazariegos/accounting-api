package com.pmmp.accounting.exception;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class AbstractServiceException extends RuntimeException {
    private String code;
    private ErrorMessage errorMessage;
    private Map<String, Object> additionalInformation;

    public AbstractServiceException(final String message,
                                    final Throwable cause,
                                    final String code,
                                    final Map<String, Object> additionalInformation,
                                    final String errorMessageKey,
                                    final Object[] errorMessageArgs) {
        super(message, cause);
        this.code = code;
        this.errorMessage = new ErrorMessage(errorMessageKey, errorMessageArgs);
        this.additionalInformation = additionalInformation;
    }
}
