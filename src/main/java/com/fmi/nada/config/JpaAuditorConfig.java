package com.fmi.nada.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA auditing enable 설정
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditorConfig {
}
