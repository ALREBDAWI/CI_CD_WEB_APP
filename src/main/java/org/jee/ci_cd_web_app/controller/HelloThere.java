package org.jee.ci_cd_web_app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloThere {
    @GetMapping("/hello_there")
    public String hello() {
        return "hello there from spring";
    }
}

