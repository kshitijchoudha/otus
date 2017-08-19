package com.otus.springboot.servicec.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerCController {
	
	@RequestMapping("/greeting")
    public String greeting() {
        return "OTUS-C";
    }

}
