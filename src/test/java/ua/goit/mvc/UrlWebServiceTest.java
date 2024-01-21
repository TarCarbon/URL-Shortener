package ua.goit.mvc;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.service.UrlServiceImpl;
import ua.goit.user.service.UserServiceImpl;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UrlWebServiceTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UrlServiceImpl urlService;

    @Autowired
    UserServiceImpl userService;
    @Autowired
    UrlRepository urlRepository;
    @Autowired
    UrlWebController urlWebController;
    @Autowired
    UrlWebService urlWebService;

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port + "/V2/urls";
    }

    @Test
    void createValidLinkTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        CreateUrlRequest request = new CreateUrlRequest("http://google.com", "Google");

        UrlDto shortUrl = urlService.createUrl(authentication.getName(), request);
        assertNotNull(shortUrl.getShortUrl());

        ModelAndView model = urlWebService.createUrl(authentication, request);

        assertEquals("all-user", model.getViewName());
        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertEquals(4, urlService.getAllUrlUser(authentication.getName()).size());
    }

    @Test
    void createInvalidLinkTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        CreateUrlRequest request = new CreateUrlRequest("http://invalid.com", "invalid link");
        ModelAndView model = new ModelAndView();
        try {
            model = urlWebService.createUrl(authentication, request);
            assertEquals("create", model.getViewName());
            assertTrue(model.getModel().containsKey("username"));
            Object usernameAttribute = model.getModel().get("username");
            assertNotNull(usernameAttribute);
            assertEquals("testadmin", usernameAttribute);
        } catch (Exception e) {
            assertTrue(model.getModel().containsKey("errors"));
            Object errorsAttribute = model.getModel().get("errors");
            assertNotNull(errorsAttribute);
            assertEquals(e.getMessage(), errorsAttribute);
        }
    }

}