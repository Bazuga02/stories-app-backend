package com.abhishek.stories_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "postgres", "local" })
class StoriesAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
