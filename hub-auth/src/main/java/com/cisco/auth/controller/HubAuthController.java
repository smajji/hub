package com.cisco.auth.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.auth.domain.Account;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class HubAuthController {

	@GetMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Account getCurrentAccount(Principal principal) {
		ObjectMapper mapper = new ObjectMapper();
		Account account = null;
		try {
			File file = new ClassPathResource("test.json").getFile();
			account = mapper.readValue(file, Account.class);
			account.setLastSeen(new Date());
			account.setName(principal.getName());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account;
	}
	//
	// @RequestMapping(value = "/current", method = RequestMethod.GET)
	// public Principal getUser(Principal principal) {
	// return principal;
	// }
}
