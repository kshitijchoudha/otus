package com.otus.springboot.servicec.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerCController {
	
	@RequestMapping("/hello")
    public String greeting() {
        return "[C]";
    }

}
