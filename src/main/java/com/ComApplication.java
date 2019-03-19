package com;

import com.model.User;
import com.model.enu.RoleEnum;
import com.repository.UserRepository;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ComApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;


/*	@Autowired
	private PasswordEncoder passwordEncoder;*/


	public static void main(String[] args) {
		SpringApplication.run(ComApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);
		String soheil = passwordEncoder.encode("soheil");
		String soheil2 = passwordEncoder.encode("soheil");
		String soheil3 = passwordEncoder.encode("soheil");
		System.out.println(soheil);
		System.out.println(soheil2);
		System.out.println(soheil3);
	}

}
