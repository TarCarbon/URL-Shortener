package ua.goit.url.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortLinkGeneratorTest {

    @Test
    public void testGenerateShortLink() {
        UrlServiceImpl urlService = Mockito.mock(UrlServiceImpl.class);


        Mockito.when(urlService.listAll()).thenReturn(
                List.of(

                )
        );

        ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator(urlService);

        String generatedLink = shortLinkGenerator.generateShortLink();

        assertTrue(generatedLink.startsWith(""));

        assertFalse(urlService.listAll().stream().anyMatch(urlDto -> urlDto.getShortUrl().equals(generatedLink)));
    }
}

