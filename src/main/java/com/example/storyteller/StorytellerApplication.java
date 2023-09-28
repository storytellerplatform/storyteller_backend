package com.example.storyteller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class StorytellerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorytellerApplication.class, args);
	}

}
