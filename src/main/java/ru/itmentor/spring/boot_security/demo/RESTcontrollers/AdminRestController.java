package ru.itmentor.spring.boot_security.demo.RESTcontrollers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserErrorResponse;
import ru.itmentor.spring.boot_security.demo.util.UserNotCreatedException;
import ru.itmentor.spring.boot_security.demo.util.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/rest")
public class AdminRestController {
    private final UserService userService;

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    //--------------------------------------------------------------------------------------------------
    @GetMapping()
    public List<User> getUsers() {
        return userService.allUsers();  //Jakson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") int id) {
        return userService.findUserById(id);
    }

    //----------------------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid User user,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder(); //обработка ошибки
            List<FieldError> errors = bindingResult.getFieldErrors(); //получаем ошибки из bindingResult, собираем в лист
            for (FieldError error : errors) { //проходимся по всем ошибкам и их конкатенируем в строку
                errorMsg.append(error.getField())//вызываем поле в котором была совершена ошибка
                        .append(" - ").append(error.getDefaultMessage()) //конкатенируем знак - , выводим какая ошика была в поле
                        .append(";");
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.saveUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    //----------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //----------------------------------------------------------------------------------------

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid User user,BindingResult bindingResult,
                         @PathVariable("id") int id){
        userService.update(id,user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //----------------------------------------------------------------------------------------
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
//в HTTP ответе будет тело ответа (response) и будет статус в заголовке HTTP ответа
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //4** статус
    }

    //----------------------------------------------------------------------------------------
    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse(
                "User with this ID wasn't found  ! (；⌣̀_⌣́)  ",
                System.currentTimeMillis()
        );
//в HTTP ответе будет тело ответа (response) и будет статус в заголовке HTTP ответа
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); //404 статус
    }
//----------------------------------------------------------------------------------------


}
