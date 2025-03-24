package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@SpringBootTest
class NexignBootcampTestCaseApplicationTests {

	@Test
	void contextLoads() {

        System.out.println(UUID.randomUUID().toString());
	}

}
