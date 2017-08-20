package com.otus.springboot.serviceg.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerGController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "OTUS-I";
    }

}
