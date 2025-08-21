package com.example.markdownnoteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MarkdownnoteappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarkdownnoteappApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate () {
		return new RestTemplate ();
	}
}
