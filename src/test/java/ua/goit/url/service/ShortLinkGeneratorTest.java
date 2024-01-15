package ua.goit.url.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ua.goit.url.UrlEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortLinkGeneratorTest {

    @Test
    public void testGenerateShortLink() {
        UrlServiceImpl urlService = Mockito.mock(UrlServiceImpl.class);


        Mockito.when(urlService.getAll()).thenReturn(
                List.of(
                        new UrlEntity("abc123", "http://example.com", "Description", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlEntity("xyz456", "http://example2.com", "Description2", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlEntity("utyt890", "http://example3.com", "Description3", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlEntity("Nblwij", "http://example4.com", "Description4", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlEntity("HdL2w1", "http://example5.com", "Description5", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0)
                )
        );

        ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator(urlService);

        String generatedLink = shortLinkGenerator.generateShortLink();

        assertTrue(generatedLink.startsWith(""));

        assertFalse(urlService.getAll().stream().anyMatch(urlEntity -> urlEntity.getShortUrl().equals(generatedLink)));
    }
}

