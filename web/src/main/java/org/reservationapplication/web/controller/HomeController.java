package org.reservationapplication.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("admin/home")
    public String adminHome() {
        return "admin_home";
    }

    @GetMapping("customer/home")
    public String customerHome() {
        return "customer_home";
    }
}
