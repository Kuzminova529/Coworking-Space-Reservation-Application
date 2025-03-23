package org.service.spring;

import org.domain.spring.DomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.service")
@Import(DomainConfig.class)
public class ServiceConfig {
}