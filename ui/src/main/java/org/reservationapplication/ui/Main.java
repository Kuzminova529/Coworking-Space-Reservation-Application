package org.reservationapplication.ui;

import org.reservationapplication.domain.sql.DatabaseMigration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args) {
        DatabaseMigration.migrate();

        ApplicationContext context = new AnnotationConfigApplicationContext(UIConfig.class);

        Menu menu = context.getBean(Menu.class);
        menu.welcomeMenu();
    }
}