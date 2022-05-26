package com.pmmp.config.persistance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class PersistenceConfig {
    @Value("accounting-api")
    private String applicationOauthClientId;

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl(applicationOauthClientId);
    }
}
