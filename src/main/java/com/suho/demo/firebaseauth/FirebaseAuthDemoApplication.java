package com.suho.demo.firebaseauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FirebaseAuthDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirebaseAuthDemoApplication.class, args);
	}

}
