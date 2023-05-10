package ru.bank.application.test.integration.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.application.Application;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.persistence.entity.user.Role;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class MainIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    JwtTokenProvider tokenProvider;

    private final String URL = "/api";
    private String token;

    @Before
    public void generateToken(){
        token = tokenProvider.createToken("test", Role.USER);
    }

    @Test
    public void accessDeniedTest() throws Exception {
        mvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    @Test
    public void accessDeniedAdminTest() throws Exception {
        token = tokenProvider.createToken("admin", Role.ADMIN);
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getMainPage_thenStatus200() throws Exception {
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }


}
