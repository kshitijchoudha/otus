package com.otus.springboot.servicef.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerFController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "OTUS-F";
    }

}
