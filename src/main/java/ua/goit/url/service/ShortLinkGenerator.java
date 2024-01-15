package ua.goit.url.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ShortLinkGenerator {
    private static final Random random = new SecureRandom();
    private final LinkUnique linkUnique;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LINK_LENGTH = 8;

    public String generateShortLink() {

        String shortLink;

        do {
            StringBuilder linkBuilder = new StringBuilder("http://localhost:9999/");
            for (int i = 0; i < LINK_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                linkBuilder.append(randomChar);
            }

            shortLink = linkBuilder.toString();
        } while (linkUnique.isLinkUnique(shortLink));

        return shortLink;
    }
}
