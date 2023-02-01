package com.coms309.demo1.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public String welcome(){
        return "Welcome to the Chess Boxing project!";
    }
}
