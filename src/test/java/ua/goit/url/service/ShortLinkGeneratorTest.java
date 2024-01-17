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
                        new UrlDto(1L,"abc123", "http://example.com", "Description", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlDto(2L,"xyz456", "http://example2.com", "Description2", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlDto(3L,"utyt890", "http://example3.com", "Description3", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlDto(4L,"Nblwij", "http://example4.com", "Description4", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0),
                        new UrlDto(5L,"HdL2w1", "http://example5.com", "Description5", null, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0)
                )
        );

        ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator(urlService);

        String generatedLink = shortLinkGenerator.generateShortLink();

        assertTrue(generatedLink.startsWith(""));

        assertFalse(urlService.listAll().stream().anyMatch(urlEntity -> urlEntity.getShortUrl().equals(generatedLink)));
    }
}

