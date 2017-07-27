package com.cisco.component.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "hub-auth")
public interface AuthServiceClient {
	@RequestMapping(method = RequestMethod.GET, value = "/uaa/getLoggedInUser", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	String getLoggedInUser(@RequestHeader("Authorization") String a);
}
