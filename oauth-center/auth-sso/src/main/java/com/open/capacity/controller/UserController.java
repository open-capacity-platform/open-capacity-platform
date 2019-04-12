package com.open.capacity.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class UserController {

	
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/message")
	public Map<String, Object> dashboard() {
		return Collections.<String, Object> singletonMap("message", "Yay!");
	}

    @GetMapping("/users")
    public Authentication user(Authentication user) {
        return user;
    }

    @RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

    
    

	
 
    
    
}
