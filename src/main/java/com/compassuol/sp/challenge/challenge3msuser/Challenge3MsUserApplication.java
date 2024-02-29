package com.compassuol.sp.challenge.challenge3msuser;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class Challenge3MsUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(Challenge3MsUserApplication.class, args);
	}

}
