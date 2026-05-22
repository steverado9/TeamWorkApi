package com.steverado.TeamWorkApi;

import com.steverado.TeamWorkApi.entity.User;
import com.steverado.TeamWorkApi.enums.Role;
import com.steverado.TeamWorkApi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TeamWorkApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TeamWorkApiApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (userRepository.findByEmail("isaac.stephen@example.com").isEmpty()) {
			String encodedPassword = passwordEncoder.encode("12345");

			User user1 = new User(
					"Stephen", "Isaac", "isaac.stephen@example.com", encodedPassword,
					"Male", Role.ADMIN, "Engineering", "7 Adekoya Street, Lagos, Nigeria"
			);
			userRepository.saveUser(user1.getFirstName(), user1.getLastName(), user1.getEmail(), user1.getPassword(), user1.getGender(), user1.getJobRole(), user1.getDepartment(), user1.getAddress());
			System.out.println("Default admin created successfully.");
		} else {
			System.out.println("Admin user already exists.");
		}


	}
}
