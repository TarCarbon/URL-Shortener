package ua.goit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShortenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ShortenerApplication.class, args);
	}

}
