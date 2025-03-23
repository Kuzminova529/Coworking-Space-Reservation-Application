package org.reservationapplication.ui;

import org.reservationapplication.service.spring.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.reservationapplication.ui")
@Import(ServiceConfig.class)
public class UIConfig {
}