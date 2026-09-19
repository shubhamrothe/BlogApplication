package com.example.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.repositories.RoleRepository;
import com.example.repositories.UserRepository;

@Configuration
public class MyConfiguration {

    @Bean
    ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	CommandLineRunner ensureRoles(RoleRepository roleRepository, UserRepository userRepository) {
		return args -> {
			Role userRole = roleRepository.findByRoleName("ROLE_USER")
					.orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
			Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
					.orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
			userRepository.findByEmail("rothe.shubham0607@gmail.com").ifPresent(user -> {
				user.getRoles().clear();
				user.getRoles().add(adminRole);
				userRepository.save(user);
			});
		};
	}
}
