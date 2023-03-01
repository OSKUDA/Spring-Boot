package com.example.oskuda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Oskar Krishna Shrestha
 * date: 3/1/2023
 * package: com.example.oskuda.controller
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public String hello(){
        return "Hi Oskar haha";
    }
}
