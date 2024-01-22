package ua.goit.mvc;

import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.request.UpdateUrlRequest;
import ua.goit.url.service.UrlServiceImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

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
    UrlWebService urlWebService;

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port + "/V2/urls";
    }

    @Test
    @DisplayName("Create valid link")
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
    @DisplayName("Create invalid link")
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
    @Test
    @DisplayName("Valid update")
    @Transactional
    void updateUrlTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UpdateUrlRequest request = new UpdateUrlRequest("edited12", "https://www.google.com/", "Edited");

        ModelAndView model = urlWebService.updateUrl(authentication, 1L, request);

        assertEquals("all-user", model.getViewName());
        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        String createdString = "2024-01-18 12:34:56";
        String expirationString = "2024-02-18 12:34:56";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate created = LocalDate.parse(createdString,formatter);
        LocalDate expiration = LocalDate.parse(expirationString,formatter);
        UrlDto editedUrlDto = new UrlDto(1L, "edited12", "https://www.google.com/", "Edited", "testadmin", created, expiration, 1);
        assertEquals(editedUrlDto, urlService.getById(1L));
    }

    @Test
    @DisplayName("Invalid update")
    @Transactional
    void editUrlWithErrorsTest() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UpdateUrlRequest request = new UpdateUrlRequest("invalid1", "https://www.invalid.com/", "Invalid");
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "UpdateUrlRequest");

        ModelAndView model = urlWebService.getEditModelAndViewWithErrors(bindingResult, request, 1L, authentication);

        assertEquals("edit", model.getViewName());
        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertTrue(model.getModel().containsKey("errors"));
        Object errorsAttribute = model.getModel().get("errors");
        assertNotNull(errorsAttribute);
    }
}