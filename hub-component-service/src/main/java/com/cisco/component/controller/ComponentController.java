package com.cisco.component.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.component.client.AuthServiceClient;
import com.cisco.component.domain.Account;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ComponentController {

	@Autowired
	AuthServiceClient authClient;

	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Account getCurrentAccount(HttpServletRequest request) {		
		String loggedInUser = authClient.getLoggedInUser(request.getHeader("Authorization"));
		ObjectMapper mapper = new ObjectMapper();
		Account account = null;
		try {
			File file = new ClassPathResource("test.json").getFile();
			account = mapper.readValue(file, Account.class);
			account.setLastSeen(new Date());
			account.setName(loggedInUser);
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

}
