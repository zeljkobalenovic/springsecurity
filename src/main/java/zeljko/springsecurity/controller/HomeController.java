package zeljko.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * HomeController
 */
@RestController
 public class HomeController {

    // kad hocemo samo obican string da vratimo korisniku na browser
    
    @GetMapping("/")                    // da bude svima dostupno ( unauthenticated )
    public String home() {                
        return "Welcome to App home";
    }

    @GetMapping("/user")                // da je dostupno onim userima sa rolama user i admin
    public String user() {                
        return "Welcome to user";
    }

    @GetMapping("/admin")               // da je dostupno samo userima sa rolom admin
    public String admin() {                
        return "Welcome to admin";
    }
}