package ru.bank.application.test.integration.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.application.Application;
import ru.bank.application.api.data.login.LoginApiRequest;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getLoginPage_thenStatus200() throws Exception {
        mvc.perform(get("/api/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Добро пожаловать в онлайн банк")))
                .andExpect(jsonPath("$.body", is("Войти можно по логину и паролю." +
                        " Если у вас нет учетной записи, предлагаем зарегистрироваться.")));
    }


    @Test
    public void loginRoleUserTest() throws Exception {
        LoginApiRequest loginApiRequest = new LoginApiRequest();
        loginApiRequest.setLogin("test");
        loginApiRequest.setPassword("12345678");
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginApiRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("USER")));

    }

    @Test
    public void loginRoleAdminTest() throws Exception {
        LoginApiRequest loginApiRequest = new LoginApiRequest();
        loginApiRequest.setLogin("admin");
        loginApiRequest.setPassword("12345678");
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginApiRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("ADMIN")))
                .andExpect(jsonPath("$.token", Matchers.containsString("Bearer_")));
    }

    @Test
    public void loginBedParametersTest() throws Exception {
        LoginApiRequest loginApiRequest = new LoginApiRequest();
        loginApiRequest.setLogin("test1");
        loginApiRequest.setPassword("12345678");
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginApiRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        loginApiRequest.setLogin("test");
        loginApiRequest.setPassword("1");
        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginApiRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

    }



}
