package org.reservationapplication.ui;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(UIConfig.class);
//
//        Flyway flyway = context.getBean(Flyway.class);
//        flyway.migrate();

        Menu menu = context.getBean(Menu.class);
        menu.welcomeMenu();
    }
}