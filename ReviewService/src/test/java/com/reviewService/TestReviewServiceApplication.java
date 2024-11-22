package com.reviewService;

import org.springframework.boot.SpringApplication;

public class TestReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ReviewApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
