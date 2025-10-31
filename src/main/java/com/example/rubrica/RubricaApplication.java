package com.example.rubrica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching 
@SpringBootApplication
public class RubricaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RubricaApplication.class, args);
	}

}
