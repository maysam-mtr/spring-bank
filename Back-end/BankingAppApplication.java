package com.example.bankingApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.example.bankingApp.models"})  // scan JPA entities
public class BankingAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(BankingAppApplication.class, args);

	}
}
