package ua.goit.url;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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

    String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJBdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaWF0IjoxNzA1NjU0NTc2LCJleHAiOjE3MDU3NDA5NzZ9.OHofMISL71EBCuQp4uMjC2pjyyXn8kgktMO0Idaf7lg";

    @BeforeEach
    void setUp(){
        RestAssured.baseURI = "http://localhost:" + port + "/V1/urls";
        UserDto userDto = userService.findByUsername("testadmin");
    }

    @Test
    @DisplayName("Get all urls")
    void testGetAllUrls() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .body(".", hasSize(4));
    }

    @Test
    @DisplayName("Create url from valid source")
    void testCreateLinkFromValidSource() {
        CreateUrlRequest request = new CreateUrlRequest("http://google.com", "test in progress");

        UrlDto createdUrl = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/create")
                .then()
                .statusCode(200)
                .extract()
                .as(UrlDto.class);
    }

    @Test
    @DisplayName("Create url from invalid source")
    void testCreateLinkFromInvalidSource() {
        CreateUrlRequest request = new CreateUrlRequest("http://invalidsorce.com", "test in progress");
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/create")
                .then()
                .assertThat()
                .body(containsString("Url with url = http://invalidsorce.com is not accessible!"));
    }

    @Test
    @DisplayName("Get all user urls")
    void testAllUserUrls() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/list/user")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    @DisplayName("Delete url")
    void testDeleteById() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/delete/1")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get all active urls")
    void activeUrls() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/list/user/active")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Get all inactive urls")
    void inactiveUrls() {
        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/list/user/inactive")
                .then()
                .statusCode(200);
    }
}