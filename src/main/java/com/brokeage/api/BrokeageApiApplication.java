package com.brokeage.api;

import com.brokeage.api.model.Role;
import com.brokeage.api.model.User;
import com.brokeage.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BrokeageApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokeageApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner testUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				User user = new User();
				user.setUsername("admin");
				user.setPassword(passwordEncoder.encode("1234"));
				user.setRole(Role.valueOf("ADMIN"));
				userRepository.save(user);
				System.out.println("✅ Admin user eklendi: admin / 1234");
			}
		};

	}
}