package org.reservationapplication.service.spring;

import org.reservationapplication.domain.spring.DomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.reservationapplication.service")
@Import(DomainConfig.class)
public class ServiceConfig {
}