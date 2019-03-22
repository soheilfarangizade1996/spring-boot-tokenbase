package com;

import com.model.User;
import com.model.enu.RoleEnum;
import com.repository.UserRepository;
import com.service.SystemConfigService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class ComApplication  implements CommandLineRunner{

	@Autowired
	private UserService userService;

	@Autowired
	private SystemConfigService systemConfigService;


/*	@Autowired
	private PasswordEncoder passwordEncoder;*/


	public static void main(String[] args) {
		SpringApplication.run(ComApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        userService.cacheUserFromDatabase();
        systemConfigService.addAllToCache();
    }
}
