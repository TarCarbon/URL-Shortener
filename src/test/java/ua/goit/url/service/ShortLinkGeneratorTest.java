package ua.goit.url.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.service.ShortLinkGenerator;
import ua.goit.url.service.UrlServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortLinkGeneratorTest {
    private List<UrlDto> mockUrls;

    @BeforeEach
    void setUp() {
        mockUrls = Arrays.asList(
                createUrlDto(1L, "abc123", "https://www.example.com/1", "null", "navi",
                        LocalDateTime.now(), LocalDateTime.now().plusDays(30L), 0),
                createUrlDto(2L, "def456", "https://www.example.com/2", "null", "Arih",
                        LocalDateTime.now(), LocalDateTime.now().plusDays(30L), 0),
                createUrlDto(3L, "uthg0909", "https://www.example.com/2", "null", "Daf",
                        LocalDateTime.now(), LocalDateTime.now().plusDays(30L), 0)
        );
    }

    private UrlDto createUrlDto(long id, String shortUrl, String url, String description, String username,
                                LocalDateTime createdDate, LocalDateTime expirationDate, int visitCount) {
        UrlDto urlDto = new UrlDto();
        urlDto.setId(id);
        urlDto.setShortUrl(shortUrl);
        urlDto.setUrl(url);
        urlDto.setDescription(description);
        urlDto.setUsername(username);
        urlDto.setCreatedDate(createdDate);
        urlDto.setExpirationDate(expirationDate);
        urlDto.setVisitCount(visitCount);
        return urlDto;
    }

    @Test
    public void testGenerateShortLink() {
        UrlServiceImpl urlService = Mockito.mock(UrlServiceImpl.class);

        Mockito.when(urlService.listAll()).thenReturn(mockUrls);

        ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator(urlService);

        String generatedLink = shortLinkGenerator.generateShortLink();


        assertTrue(generatedLink.startsWith(""));


        assertFalse(urlService.listAll().stream().anyMatch(urlEntity -> urlEntity.getShortUrl().equals(generatedLink)));
    }
}
