package com.security.feature;

import com.security.feature.config.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SecurityProjectApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnv();
		SpringApplication.run(SecurityProjectApplication.class, args);
	}

}
