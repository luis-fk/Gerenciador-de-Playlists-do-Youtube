package com.redplay.redplay;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import com.redplay.redplay.managers.ObjectManager;

@SpringBootTest
class SpringBootTests {
	final static ObjectManager objManager = new ObjectManager();

	@Test
	void contextLoads() {
	}
}
