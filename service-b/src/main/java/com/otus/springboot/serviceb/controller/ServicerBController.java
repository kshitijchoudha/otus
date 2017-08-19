package com.otus.springboot.serviceb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerBController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "Madhu";
    }

}
