package com.jhnr.myclub;

import com.jhnr.myclub.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService) {
		return args -> {
			/*
			System.out.println("AJOUT DES ROLES ET USERS DEFAULT");

			AppRole roleUser = new AppRole(null, "USER");
			AppRole roleAdmin = new AppRole(null, "ADMIN");
			AppRole roleTrainer = new AppRole(null, "TRAINER");

			accountService.addRole(roleUser);
			accountService.addRole(roleAdmin);
			accountService.addRole(roleTrainer);

			AppUser admin = new AppUser(null, "admin", "1234", "admin@ynov.com", "012345",
					"image/path", "admin", "test", null, null, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

			accountService.addUser(admin);

			accountService.addRoleToUser(admin.getUserName(), roleUser.getRoleName());
			accountService.addRoleToUser(admin.getUserName(), roleTrainer.getRoleName());
			accountService.addRoleToUser(admin.getUserName(), roleAdmin.getRoleName());

			System.out.println("FIN DES AJOUTS");

			 */

		};
	}
}
