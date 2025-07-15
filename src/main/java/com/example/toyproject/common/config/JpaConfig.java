package com.example.toyproject.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // This class is used to enable JPA auditing features in the application.
    // It allows for automatic population of auditing fields like createdDate, lastModifiedDate, etc.
    // No additional beans or configurations are needed here as the annotation takes care of it.
}