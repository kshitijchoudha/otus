package com.otus.springboot.serviced.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerDController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "OTUS-D";
    }

}
