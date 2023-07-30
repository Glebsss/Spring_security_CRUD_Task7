package ru.itmentor.spring.boot_security.demo.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class) //указываем окружение в которому будут стартовать тесты
@SpringBootTest

//указывает что запускаем тесты в окружении спринг бут приложения(автоматически пойдёт в папки и найдёт все необходимые конфигурациии)
class UserServiceTestCreate {
    @Autowired //Сервис, который хотим протестировать
    private UserService userService;


    @MockBean
    private UserRepository userRepository; //имитируем наличие
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void saveUser() {
        User user = new User("user", 31, "user@mail.ru");
        boolean isUserCreated = userService.saveUser(user);

        assertTrue(isUserCreated);
        Assert.assertNotNull(user.getUsername());
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.getAge());
        Assert.assertNotNull(user.getEmail());
    }

    @Test
    public void saveUserFailTest() { //тест на создание юзера с занятым юзернеймом
        User user = new User();
        user.setUsername("Gleb");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("Gleb"); //возвращаем нового пользователя когда на репозитории
        // пытаемся создать пользователя с таким аргументом
        //нужно с помощью mockito имитировать нужное нам поведение(нахождение пользователя в базе)
        boolean isUserCreated = userService.saveUser(user);

        Assert.assertFalse(isUserCreated);
    }
}