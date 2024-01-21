package ua.goit.mvc;

import io.restassured.RestAssured;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.repository.UrlRepository;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.service.UrlServiceImpl;
import ua.goit.url.service.exceptions.NotAccessibleException;
import ua.goit.user.service.UserServiceImpl;

import java.security.Principal;
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
    UserServiceImpl userService;
    @Autowired
    UrlRepository urlRepository;
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
    void getAllActiveLinks() {
        ModelAndView result = urlWebController.getAllActiveLinks();

        assertEquals("all-guest", result.getViewName());
        assertTrue(result.getModel().containsKey("userUrls"));
        assertEquals(4, urlService.getActiveUrl().size());
    }

    @Test
    void getAllInactiveLinks() {
        ModelAndView result = urlWebController.getAllInactiveLinks();

        assertEquals("all-guest", result.getViewName());
        assertTrue(result.getModel().containsKey("userUrls"));
        assertEquals(0, urlService.getInactiveUrl().size());
    }

    @Test
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
        assertEquals(2, urlService.getAllUrlUser("testadmin").size());
    }

    /*@Test
    @DisplayName("Delete link")
    void delete() {
        urlService.deleteById("testadmin", 1L);
        assertThrows(IllegalArgumentException.class, () -> urlService.deleteById("testadmin", 1L));

//        assertEquals(4, urlService.listAll().size());
    }*/

    @Test
    @DisplayName(" Get all users active links")
    void getAllUsersActiveLinks() {
        assertEquals(3, urlService.getActiveUrlUser("testadmin").size());
    }

    @Test
    @DisplayName(" Get all users inactive links")
    void getAllUsersInactiveLinks() {
        assertEquals(0, urlService.getInactiveUrlUser("testadmin").size());
    }

    @Test
    void edit() {
    }

    @Test
    void postEdit() {
    }



    @Test
    void prolongation() {
    }




}