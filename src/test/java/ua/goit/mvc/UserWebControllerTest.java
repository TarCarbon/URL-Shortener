package ua.goit.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.goit.user.CreateUserRequest;

import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.web.servlet.ModelAndView;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class UserWebControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    UserWebService userWebService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserWebController userWebController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port + "/V2/user";
    }


    @Test
    @DisplayName("test to open a page for registration")
    public void openRegistrationPageTest() throws Exception {
        this.mockMvc.perform(get("/V2/user/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("register")));
    }


    @Test
    @DisplayName("status OK when enter correctly data")
    public void postRegisterUserTest() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user");
        userRequest.setPassword("Password123");

        this.mockMvc.perform(post("/V2/user/register")
                        .contentType("application/json")
                        .param("register", "true")
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("error when enter not correctly name")
    public void postRegisterUserBadNameTest() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("Password123");

        BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "CreateUserRequest");
        bindingResult.rejectValue("user", "user.exist", "user name or password not corrected");
        mockMvc.perform(post("/V2/user/register?register=true")
                        .param("register", "true")
                        .contentType("application/x-www-form-urlencoded")
                        .flashAttr("userRequest", userRequest)
                        .flashAttr("org.springframework.validation.BindingResult.userRequest", bindingResult)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("test to open a page for login")
    public void getLoginTest() throws Exception {
        this.mockMvc.perform(get("/V2/user/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("login")));
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("test login")
    public void postLoginTest() {
        ResponseEntity<String> registrationResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/V2/user/register",
                new CreateUserRequest("testUser", "testPassword123"),
                String.class);

        assertThat(registrationResponse.getStatusCodeValue()).isEqualTo(200);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/V2/user/login",
                new CreateUserRequest("testUser", "testPassword123"),
                String.class);

        assertThat(loginResponse.getStatusCodeValue()).isEqualTo(200);
    }

}
