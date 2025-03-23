package org.ui;

import org.service.spring.ServiceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.ui")
@Import(ServiceConfig.class)
public class UIConfig {
}