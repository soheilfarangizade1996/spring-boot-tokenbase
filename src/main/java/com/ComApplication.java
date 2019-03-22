package com;


import com.service.SystemConfigService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ComApplication  implements CommandLineRunner{

	@Autowired
	private UserService userService;

	@Autowired
	private SystemConfigService systemConfigService;




	public static void main(String[] args) {
		SpringApplication.run(ComApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        userService.cacheUserFromDatabase();
        systemConfigService.addAllToCache();
    }
}
