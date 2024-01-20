package ua.goit.user;

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
import ua.goit.user.service.UserServiceImpl;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/V1/user";
    }

    @Test
    @DisplayName("Register new user")
    void registerUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newUser");
        request.setPassword("Password9");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Register new user with too short password")
    void testRegisterUserWithUncorrect() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("postgres");
        request.setPassword("123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .assertThat().body(containsString("Password should have 8 or more characters and contains numbers, " +
                        "letters in upper case and letters in lower case"));
    }

    @Test
    @DisplayName("Login existing user")
    void testLoginUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testadmin");
        request.setPassword("qwerTy12");

        String loginUser = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .extract().body().asString();
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0YWRtaW4iLCJBdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiVVNFUiJ9XSwiaWF0IjoxNzA1NjU0NTc2LCJleHAiOjE3MDU3NDA5NzZ9.OHofMISL71EBCuQp4uMjC2pjyyXn8kgktMO0Idaf7lg";

        assertThat(loginUser.matches(jwtToken));
    }

    @Test
    @DisplayName("Bad login credentials")
    void testBadloginUserCredentials() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testadmin");
        request.setPassword("password");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .assertThat()
                .body(containsString("Password should have 8 or more characters and contains numbers, " +
                        "letters in upper case and letters in lower case"));
    }
}