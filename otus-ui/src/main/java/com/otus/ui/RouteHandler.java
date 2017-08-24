package com.otus.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteHandler {
	@RequestMapping({
		"/us-east-2/**"
	})
	public String redirect() {
		return "forward:/";
	}
}
