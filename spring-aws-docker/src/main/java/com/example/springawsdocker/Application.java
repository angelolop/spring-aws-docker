package com.example.springawsdocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		 /*
		 Map<String, PasswordEncoder> encoders = new HashMap<>();
		 encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		 DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		 passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());

		 String result = passwordEncoder.encode("admin234");
		 System.out.println("My hash " + result);
		 */

	}
}
