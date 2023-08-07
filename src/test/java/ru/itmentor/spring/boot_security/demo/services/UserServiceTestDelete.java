package ru.itmentor.spring.boot_security.demo.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmentor.spring.boot_security.demo.models.User;

@RunWith(SpringRunner.class) //указываем окружение в которому будут стартовать тесты
@SpringBootTest
class UserServiceTestDelete {


    @Autowired
    private UserService userService;


    @Test
    void deleteUser() {
        User user = new User("Test4", 38, "test@mail.ru");
        user.setPassword("password");

        boolean isUserCreated = userService.saveUser(user);
        Integer id = user.getId();

        User user1 =(User) userService.loadUserByUsername(user.getUsername());
        boolean isUserDeleted = userService.deleteUser(user1.getId());

        Assert.assertTrue(isUserDeleted);
    }

}