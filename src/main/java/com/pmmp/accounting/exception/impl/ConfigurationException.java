package com.pmmp.accounting.exception.impl;

import com.pmmp.accounting.exception.AbstractServiceException;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString(callSuper = true)
public class ConfigurationException extends AbstractServiceException {
    private static final String CODE = "001-0600";

    private ConfigurationException(final String message,
                                   final Throwable cause,
                                   final Map<String, Object> additionalInformation,
                                   final String errorMessageKey,
                                   final Object[] errorMessageArgs) {
        super(message, cause, CODE, additionalInformation, errorMessageKey, errorMessageArgs);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private Throwable cause;
        private Map<String, Object> additionalInformation;
        private String errorMessageKey;
        private Object[] errorMessageArgs;

        public Builder message(final String message) {
            this.message = message;
            return this;
        }

        public Builder cause(final Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder addAdditionalInformation(final String key, final Object value) {
            if (additionalInformation == null) {
                additionalInformation = new HashMap<>();
            }

            additionalInformation.put(key, value);

            return this;
        }

        public Builder errorMessageKey(final String errorMessageKey) {
            this.errorMessageKey = errorMessageKey;
            return this;
        }

        public Builder errorMessageKey(final Object... errorMessageArgs) {
            this.errorMessageArgs = errorMessageArgs;
            return this;
        }

        public ConfigurationException build() {
            return new ConfigurationException(message, cause, additionalInformation, errorMessageKey, errorMessageArgs);
        }
    }
}
