package ru.bank.application.test.integration.registration;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.application.Application;
import ru.bank.application.api.data.registration.RegistrationApiRequest;



import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class RegistrationIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void registrationUserTest() throws Exception {
        RegistrationApiRequest request = new RegistrationApiRequest();
        request.setLogin("AaBb" + generateRandomNumbers());
        request.setName("Victor");
        request.setPatronymic("Victorovich");
        request.setSurname("Victorov");
        request.setPassword("12345678");

        mvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Пользователь зарегистрирован")));
    }

    @Test
    public void bedParametersRegistrationUserTest() throws Exception {
        RegistrationApiRequest  request = new RegistrationApiRequest();
        request.setLogin("AaBb");
        postToRegistration_thenBadRequest(request);
        request.setName("Victor");
        postToRegistration_thenBadRequest(request);
        request.setPatronymic("Victorovich");
        postToRegistration_thenBadRequest(request);
        request.setSurname("Victorov");
        postToRegistration_thenBadRequest(request);
        request.setPassword("123");
        postToRegistration_thenBadRequest(request);
    }




    private String generateRandomNumbers(){
        return "" + (int) (Math.random() * 10000);
    }

    private void postToRegistration_thenBadRequest(RegistrationApiRequest request) throws Exception {
        mvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}

