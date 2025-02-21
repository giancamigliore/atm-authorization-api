package com.itti.digital.atm.atm_authorization_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AtmAuthorizationApiApplication {
	public final static String AUTH_CACHE_NAME = "authentication";
	public final static String SUCCESS = "Successful Operation";
	public final static String CREATED = "Created";
	public final static String BAD_REQUEST = "Bad Request";
	public final static String UNAUTHORIZED = "Unauthorized";
	public final static String NO_CONTENT = "No content";
	public final static String NOT_FOUND = "Not found";
	public final static String PARAM_INVALID = "Params invalid";
	public final static String BUSINESS_ENTITY_FAILURE = "Business Entity Failure";
	public final static String UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";
	public final static String METHOD_NOT_ALLOWED = "Method Not Allowed";
	public final static String INTERNAL_SERVER_ERROR = "Internal Server Error";

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(AtmAuthorizationApiApplication.class, args);
	}

}
