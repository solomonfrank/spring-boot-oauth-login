package com.example.springOAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringOAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOAuthApplication.class, args);
	}

}
