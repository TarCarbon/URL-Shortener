package ua.goit.mvc;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.user.CreateUserRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserWebServiceTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserWebService userWebService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port + "/V2/user";
    }

    @Test
    @DisplayName("test register user service")
    public void registerUserTest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("User");
        userRequest.setPassword("Password123");

        ModelAndView model = userWebService.registerUser(userRequest);

        assertEquals("success", model.getViewName());
    }

    @Test
    @DisplayName("test for an incorrectly entered name during registration ")
    public void registerUserNameErrorsTest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("Password123");

        ModelAndView model = userWebService.registerUser(userRequest);

        assertEquals("register", model.getViewName());
        assertTrue(model.getModel().containsKey("errors"));
    }

    @Test
    @DisplayName("test for an incorrectly entered password during registration ")
    public void registerUserPasswordErrorsTest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user");

        ModelAndView model = userWebService.registerUser(userRequest);

        assertEquals("register", model.getViewName());
        assertTrue(model.getModel().containsKey("errors"));
    }

    @Test
    @DisplayName("test success login")
    void testLoginUserSuccess() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Username");
        userRequest.setPassword("Password123");

        mockMvc.perform(post("/V2/user/register")
                        .param("username", "Username")
                        .param("password", "Password123"))
                .andExpect(status().isOk());

        ModelAndView result = userWebService.loginUser(userRequest);

        assertEquals("all-user", result.getViewName());
    }

    @Test
    @DisplayName("test GetModelAndViewWithErrors method")
    void testGetModelAndViewWithErrors() throws Exception {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("field", "error message")));

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("User");
        userRequest.setPassword("Password123");

        mockMvc.perform(post("/V2/user/register")
                        .param("username", "Username")
                        .param("password", "Password123"))
                .andExpect(status().isOk());

        ModelAndView result = userWebService.loginUser(userRequest);

        result = userWebService.getModelAndViewWithErrors(bindingResult, result);

        assertEquals("login", result.getViewName());

    }

}
