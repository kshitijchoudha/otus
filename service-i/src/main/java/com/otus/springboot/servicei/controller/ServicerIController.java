package com.otus.springboot.servicei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicerIController {
	
	@RequestMapping("/hello")
    public String greeting() {
        return "[I]";
    }

}
