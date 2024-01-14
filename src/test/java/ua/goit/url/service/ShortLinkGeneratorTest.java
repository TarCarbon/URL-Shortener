package ua.goit.url.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.goit.url.service.LinkUnique;
import ua.goit.url.service.ShortLinkGenerator;
import ua.goit.url.service.UrlServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortLinkGeneratorTest {

    @Test
    public void testGenerateShortLink() {

        UrlServiceImpl urlService = Mockito.mock(UrlServiceImpl.class);
        Mockito.when(urlService.getAllShortUrls()).thenReturn(

                List.of("http://localhost:9999/abc123", "http://localhost:9999/xyz456", "http://localhost:9999/utyt890"
                        , "http://localhost:9999/Nblwij", "http://localhost:9999/HdL2w1")
        );

        LinkUnique linkUnique = new LinkUnique(urlService);
        ShortLinkGenerator shortLinkGenerator = new ShortLinkGenerator(linkUnique);


        String generatedLink = shortLinkGenerator.generateShortLink();


        assertTrue(generatedLink.startsWith("http://localhost:9999/"));
        assertFalse(urlService.getAllShortUrls().contains(generatedLink));
    }
}
