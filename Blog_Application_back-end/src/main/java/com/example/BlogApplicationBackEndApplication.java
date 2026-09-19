package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.entities.User;
import com.example.repositories.UserRepository;

@SpringBootApplication
public class BlogApplicationBackEndApplication implements CommandLineRunner{

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogApplicationBackEndApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		List<User> users = this.userRepository.findAll();
		for (User user : users) {
			String storedPassword = user.getPassword();
			if (storedPassword != null && !storedPassword.startsWith("$2")) {
				user.setPassword(this.passwordEncoder.encode(storedPassword));
				this.userRepository.save(user);
			}
		}
	}

	
}
