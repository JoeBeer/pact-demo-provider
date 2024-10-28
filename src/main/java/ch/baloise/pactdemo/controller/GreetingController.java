package ch.baloise.pactdemo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public Greeting greeting() {
        return new Greeting("Hello, World!");
    }

    @Setter
    @Getter
    @RequiredArgsConstructor
    public static class Greeting {
        private final String message;

    }
}
