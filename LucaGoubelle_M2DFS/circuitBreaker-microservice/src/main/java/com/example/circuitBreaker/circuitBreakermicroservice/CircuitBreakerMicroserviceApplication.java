package com.example.circuitBreaker.circuitBreakermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@EnableCircuitBreaker
@RestController
public class CircuitBreakerMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerMicroserviceApplication.class, args);
	}

}
