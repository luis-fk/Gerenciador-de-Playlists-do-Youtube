package com.redplay.redplay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import com.redplay.redplay.managers.ObjectManager;

@SpringBootTest
class SpringBootTests {
	final static ObjectManager objManager = new ObjectManager();
	// @Test
	// void contextLoads() {
	// }

	@Test
	void readFileTest() {
		try {
			assertNotNull(
					objManager.readFile("./src/main/java/com/syncfit/syncfit/resources/test.json"),
					"Não conseguiu ler arquivo!");

		} catch (Exception e) {
			Assertions.fail("An exception occurred!", e);
		}
	}
}
