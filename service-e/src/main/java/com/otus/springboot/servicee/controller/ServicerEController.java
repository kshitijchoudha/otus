package com.otus.springboot.servicee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerEController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "OTUS-E";
    }

}
