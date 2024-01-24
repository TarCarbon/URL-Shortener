package ua.goit.url.service;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import ua.goit.url.UrlEntity;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.mapper.UrlMapper;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.exceptions.NotAccessibleException;
import ua.goit.user.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private UrlMapper urlMapper;

    @Mock
    private ShortLinkGenerator shortLinkGenerator;

    @Mock
    private UserService userService;

    @InjectMocks
    private UrlServiceImpl urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        urlService = new UrlServiceImpl(urlMapper, urlRepository, shortLinkGenerator, userService);
    }

    @Test
    void testListAll() {
        when(urlRepository.findAll()).thenReturn(Arrays.asList(new UrlEntity(), new UrlEntity()));
        when(urlMapper.toUrlDtos(anyList())).thenReturn(Arrays.asList(new UrlDto(), new UrlDto()));

        List<UrlDto> result = urlService.listAll();

        assertEquals(2, result.size());
    }
    @Test
    void testDeleteById() {
        Long id = 1L;
        String username = "testUser";

        when(urlRepository.existsById(id)).thenReturn(true);
        doNothing().when(urlRepository).deleteById(id);

        assertDoesNotThrow(() -> urlService.deleteById(username, id));
    }



}
