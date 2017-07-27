package com.cisco.auth.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubAuthController {

	@GetMapping("/user")
	public Principal user(Principal principal) {
		return principal;
	}
	
	@GetMapping("/getLoggedInUser")
	public String getLoggedInUser(Principal principal) {
		return principal.getName();
	}

}
