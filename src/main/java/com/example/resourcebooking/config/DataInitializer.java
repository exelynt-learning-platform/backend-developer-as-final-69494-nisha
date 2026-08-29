package com.example.resourcebooking.config;

import com.example.resourcebooking.entity.User;
import com.example.resourcebooking.enums.Role;
import com.example.resourcebooking.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("Admin@123")
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("user")) {

                User user = new User();

                user.setUsername("user");
                user.setPassword(
                        passwordEncoder.encode("User@123")
                );
                user.setRole(Role.USER);

                userRepository.save(user);
            }
        };
    }
}