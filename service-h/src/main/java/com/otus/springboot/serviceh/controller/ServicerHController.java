package com.otus.springboot.serviceh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerHController {
	
	@RequestMapping("/hello")
    public String greeting() {
        return "[H]";
    }

}
