package com.tfg.cultura.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.tfg.cultura.api.config.MockConfig;

@Import(MockConfig.class)
@SpringBootTest
class ApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
