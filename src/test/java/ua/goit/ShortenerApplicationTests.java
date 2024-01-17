package ua.goit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.goit.url.UrlController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ShortenerApplicationTests {
	@Autowired
	private UrlController controller;

	@Test
	void contextLoads() {
			assertThat(controller).isNotNull();
	}
}


