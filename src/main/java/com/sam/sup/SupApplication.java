package com.sam.sup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SupApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupApplication.class, args);
	}

}
