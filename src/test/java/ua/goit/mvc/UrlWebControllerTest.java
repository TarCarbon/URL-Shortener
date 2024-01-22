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
class UrlWebControllerTest {
    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UrlServiceImpl urlService;
    @Autowired
    UrlWebController urlWebController;

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port + "/V2/urls";
    }

    @Test
    void getIndexPage() {
        String result = urlWebController.getIndexPage();
        assertEquals("index", result);
    }

    @Test
    void getIndexPageForUser() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView result = urlWebController.getIndexPageForUser(authentication);

        assertEquals("index-user", result.getViewName());
        assertTrue(result.getModel().containsKey("username"));
        Object usernameAttribute = result.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);
    }

    @Test
    @DisplayName("Get all links")
    void getAllLinks() {
        ModelAndView result = urlWebController.getAllLinks();

        assertEquals("all-guest", result.getViewName());
        assertTrue(result.getModel().containsKey("userUrls"));
        assertEquals(4, urlService.listAll().size());
    }

    @Test
    @DisplayName("Get all active links")
    void getAllActiveLinks() {
        ModelAndView result = urlWebController.getAllActiveLinks();

        assertEquals("all-guest", result.getViewName());
        assertTrue(result.getModel().containsKey("userUrls"));
        assertEquals(4, urlService.getActiveUrl().size());
    }

    @Test
    @DisplayName("Get all inactive links")
    void getAllInactiveLinks() {
        ModelAndView result = urlWebController.getAllInactiveLinks();

        assertEquals("all-guest", result.getViewName());
        assertTrue(result.getModel().containsKey("userUrls"));
        assertEquals(0, urlService.getInactiveUrl().size());
    }

    @Test
    @DisplayName("Show create page")
    void showsCreatePage() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView model = urlWebController.create(authentication);

        assertEquals("create", model.getViewName());
        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);
    }

    @Test
    @DisplayName("Create valid link")
    void createValidLink() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        CreateUrlRequest request = new CreateUrlRequest("http://google.com", "Google");
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "CreateUrlRequest");

        ModelAndView model = urlWebController.postCreate(request, bindingResult, authentication);

        assertEquals("all-user", model.getViewName());
        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertEquals(3, urlService.getAllUrlUser(authentication.getName()).size());
    }

    @Test
    @DisplayName("Create invalid link")
    void createInvalidLink() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        CreateUrlRequest request = new CreateUrlRequest("http://invalid.com", "invalid link");
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "CreateUrlRequest");
        bindingResult.rejectValue("url", "url.empty", "URL cannot be empty");

        ModelAndView model = new ModelAndView();
        try {
            model = urlWebController.postCreate(request, bindingResult, authentication);
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
    @DisplayName("Get all user links")
    void getAllUsersLinks() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView model = urlWebController.getAllUsersLinks(authentication);

        assertEquals("all-user", model.getViewName());

        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertTrue(model.getModel().containsKey("userUrls"));
        assertEquals(2, urlService.getAllUrlUser("testadmin").size());
    }

    @Test
    @DisplayName("Get all users active links")
    void getAllUsersActiveLinks() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView model = urlWebController.getAllUsersActiveLinks(authentication);

        assertEquals("all-user", model.getViewName());

        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertTrue(model.getModel().containsKey("userUrls"));
        assertEquals(3, urlService.getActiveUrlUser("testadmin").size());
    }

    @Test
    @DisplayName("Get all users inactive links")
    void getAllUsersInactiveLinks() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView model = urlWebController.getAllUsersInactiveLinks(authentication);

        assertEquals("all-user", model.getViewName());

        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertTrue(model.getModel().containsKey("userUrls"));
        assertEquals(0, urlService.getInactiveUrlUser("testadmin").size());
    }

    @Test
    @DisplayName("Show Edit page")
    @Transactional
    void showEditPage() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        ModelAndView model = urlWebController.edit(1L, authentication);

        assertEquals("edit", model.getViewName());

        assertTrue(model.getModel().containsKey("username"));
        Object usernameAttribute = model.getModel().get("username");
        assertNotNull(usernameAttribute);
        assertEquals("testadmin", usernameAttribute);

        assertTrue(model.getModel().containsKey("id"));
        Object idAttribute = model.getModel().get("id");
        assertNotNull(idAttribute);
        assertEquals(1L, idAttribute);

        assertTrue(model.getModel().containsKey("urls"));
        Object urlsAttribute = model.getModel().get("urls");
        assertNotNull(urlsAttribute);

        String createdString = "2024-01-18 12:34:56";
        String expirationString = "2024-02-18 12:34:56";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate created = LocalDate.parse(createdString,formatter);
        LocalDate expiration = LocalDate.parse(expirationString,formatter);

        UrlDto urlDto = new UrlDto(1L, "testurl1", "https://some_long_named_portal.com/", "for test only", "testadmin", created, expiration, 1);
        assertEquals(urlDto, urlService.getById(1L));
    }

    @Test
    @DisplayName("Edit link")
    @Transactional
    void postEdit() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UpdateUrlRequest request = new UpdateUrlRequest("edited12", "https://www.google.com/", "Edited");
        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "UpdateUrlRequest");

        ModelAndView model = urlWebController.postEdit(request, bindingResult, 1L, authentication);

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
    @DisplayName("Prolong link")
    @Transactional
    void prolongation() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UrlDto urlDto = urlService.getById(1L);
        LocalDate expirationDate = urlDto.getExpirationDate();

        urlWebController.prolongation(1L, authentication);
        UrlDto urlDtoProlonged = urlService.getById(1L);
        LocalDate expirationDateProlonged = urlDtoProlonged.getExpirationDate();

        assertEquals(expirationDateProlonged, expirationDate.plusDays(10));
    }

@Test
    @DisplayName("Delete link")
    void delete() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testadmin", "qwerTy12");
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        urlWebController.delete(2L, authentication);
        assertEquals(4, urlService.listAll().size());
    }
}