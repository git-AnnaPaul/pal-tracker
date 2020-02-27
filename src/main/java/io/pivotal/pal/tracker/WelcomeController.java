package io.pivotal.pal.tracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class WelcomeController {

    private final String msg;

    public WelcomeController(@Value("${welcome.message}") String message){
        this.msg = message;
    }

    @GetMapping("/")
    public String sayHello() {
        return msg;
    }
}