package org.reservationapplication;

import org.reservationapplication.domain.spring.DomainConfig;
import org.reservationapplication.domain.sql.DatabaseConfigJPA;
import org.reservationapplication.service.spring.ServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.reservationapplication")
@Import({ServiceConfig.class,DatabaseConfigJPA.class})
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }
}
