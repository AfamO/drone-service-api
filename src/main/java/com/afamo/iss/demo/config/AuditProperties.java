package com.afamo.iss.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("audit")
@Data
@Configuration
public class AuditProperties {

    @Value("${audit.creation_rate}")
    private Long creationRate;

    @Value("${audit.retrieval_rate}")
    private Long retrievalRate;
}
