package com.example.storyteller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class StorytellerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorytellerApplication.class, args);
	}

	// 套入這個，就可以用 SecurityContextHolder.getContext().getAuthentication()
	@Bean
	public AuditorAware<String> auditorProvider() {
		// 這應該返回當前登入的用戶，下面只是一個簡單的例子
		return () -> Optional.of("defaultUser");
	}
}
