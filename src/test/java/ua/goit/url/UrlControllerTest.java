package ua.goit.url;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.goit.url.dto.UrlDto;
import ua.goit.url.request.CreateUrlRequest;
import ua.goit.url.service.UrlServiceImpl;
import ua.goit.url.service.exceptions.NotAccessibleException;
import ua.goit.user.dto.UserDto;
import ua.goit.user.service.UserServiceImpl;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers

class UrlControllerTest {
    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UrlServiceImpl urlService;

    @Autowired
    UserServiceImpl userService;

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port + "/V1/urls";
        UserDto userDto = userService.findByUsername("testadmin");
    }

    @Test
    @DisplayName("Get all urls")
    void testGetAllUrls(){
        assertEquals(4, urlService.listAll().size());
    }

    @Test
    @DisplayName("Create url from valid source")
    void testCreateLinkFromValidSource() {
        CreateUrlRequest request = new CreateUrlRequest("http://google.com", "valid link");
        UrlDto shortUrl = urlService.createUrl("testadmin", request);
        assertNotNull(shortUrl.getShortUrl());
    }

    @Test
    @DisplayName("Create url from invalid source")
    void testCreateLinkFromInvalidSource() {
        CreateUrlRequest request = new CreateUrlRequest("http://invalidsorce.com", "test in progress");
        try {
            UrlDto shortUrl = urlService.createUrl("testadmin", request);
        } catch (NotAccessibleException e) {
            assertEquals("Url with url = http://invalidsorce.com is not accessible!", e.getMessage());
        }
    }

    @Test
    @DisplayName("Get all user urls")
    void testAllUserUrls() {
        assertEquals(2, urlService.getAllUrlUser("testadmin").size());
    }

    @Test
    @DisplayName("Delete url")
    void testDeleteById() {
        urlService.deleteById("testadmin", 1L);
        assertNotEquals(4, urlService.listAll().size());
    }

    @Test
    @DisplayName(" Get all active urls")
    void testActiveUrls() {
        assertEquals(3, urlService.getActiveUrlUser("testadmin").size());
    }

    @Test
    @DisplayName(" Get all in active urls")
    void testInactiveUrls() {
        assertEquals(0, urlService.getInactiveUrlUser("testadmin").size());
    }
}