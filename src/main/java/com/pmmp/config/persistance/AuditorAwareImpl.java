package com.pmmp.config.persistance;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    private final String defaultAuditor;

    public AuditorAwareImpl(final String defaultAuditor) {
        this.defaultAuditor = defaultAuditor;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(defaultAuditor);
    }
}