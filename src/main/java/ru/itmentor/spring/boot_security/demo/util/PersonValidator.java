package ru.itmentor.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.UserService;


@Component
public class PersonValidator implements Validator {

    private final UserService userService;

    @Autowired
    public PersonValidator(UserService userService) {
        this.userService = userService;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        try {
            userService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return; //все ок, пользователь с таким именем не найден
        }
        errors.rejectValue("username","","Человек с таким именем пользователя уже существует");
    }
}
