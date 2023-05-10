package ru.bank.application.test.module.user;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bank.application.Application;
import ru.bank.application.dto.user.SaveUserDto;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;



    @Test
    public void registrationTest(){
        SaveUserDto userDto = new SaveUserDto();
        userDto.setLogin("test" + generateRandomNumbers());
        userDto.setName("Тест");
        userDto.setPatronymic("Тестович");
        userDto.setSurname("Тестов");
        userDto.setPassword("12345678");
        User user = userService.registration(userDto);
        Assert.assertNotNull(user);
        Assert.assertTrue(CoreMatchers.is(user.getRole()).matches(Role.USER));
        Assert.assertEquals("Тест", userDto.getName());
        Assert.assertEquals("Тестович", userDto.getPatronymic());
        Assert.assertEquals("Тестов", userDto.getSurname());
    }

    @Test
    public void loadUserByLoginTest(){
        User user = userService.loadUserByLogin("test");
        Assert.assertNotNull(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void registrationBedParametersTest(){
        SaveUserDto userDto = new SaveUserDto();
        userDto.setLogin("test");
        userDto.setName("Тест");
        userDto.setPatronymic("Тестович");
        userDto.setSurname("Тестов");
        userDto.setPassword("12345678");
        User user = userService.registration(userDto);

    }

    private String generateRandomNumbers(){
        return "" + (int) (Math.random() * 10000);
    }

}
