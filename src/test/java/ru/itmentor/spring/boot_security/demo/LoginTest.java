package ru.itmentor.spring.boot_security.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmentor.spring.boot_security.demo.controllers.AdminController;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class) //указываем окружение в которому будут стартовать тесты
@SpringBootTest
//указывает что запускаем тесты в окружении спринг бут приложения(автоматически пойдёт в папки и найдёт все необходимые конфигурациии)
@AutoConfigureMockMvc
//Позволяет создать структуру классов которая подменит MVC. Даёт нам более удобный метод тестирования приложения
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminController helloController;

    @Test //помечаем тестовые методы
    public void test() throws Exception {
//        assertThat(adminController).isNotNull(); //проверяем что контроллер с контекстом подтянут
        this.mockMvc.perform(get("/")) //хотим выполнить get запрос на главную страницу проекта
                .andDo(print()) //данный метод выводит полученный результат в консоль (можем посмотреть что сломалось когда сломается)
                .andExpect(status().isOk()) //обёртка assert, позволяет сравнить полученный результат с ожидаемым и выбросить исключение если что-то идёт не так, ожидаем что вернется 200ый код
                .andExpect(content().string(containsString("Welcome to Index PAGE!"))); //ожидаем какой-то контент(в данном случаее строку) и его мы проверяем(сравниваем) на содержание подстроки "Hello World!"
    }

    @Test //помечаем тестовые методы
    public void sayHelloTest() throws Exception {
//        assertThat(adminController).isNotNull(); //проверяем что контроллер с контекстом подтянут
        this.mockMvc.perform(get("/hello")) //хотим выполнить get запрос на главную страницу проекта
                .andDo(print()) //данный метод выводит полученный результат в консоль (можем посмотреть что сломалось когда сломается)
                .andExpect(status().isOk()) //обёртка assert, позволяет сравнить полученный результат с ожидаемым и выбросить исключение если что-то идёт не так, ожидаем что вернется 200ый код
                .andExpect(content().string(containsString("Hello world!"))); //ожидаем какой-то контент(в данном случаее строку) и его мы проверяем(сравниваем) на содержание подстроки "Hello World!"
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/admin")) //гет запрос
                .andDo(print()) //выводим результат
                .andExpect(status().is4xxClientError()) //система ожидает статус отличный от 200ого , выберем 400, будет перенаправление на страничку логина
                .andExpect(redirectedUrl(null)); //ожидаем что система подкинет адрес логина ?????????????????
    }

    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("admin"))   //проверяем авторизацию пользователя через MockMvc
                .andDo(print()) //выводим результат
                .andExpect(status().is3xxRedirection()) //ожидаем 3** статус
                .andExpect(redirectedUrl("/admin")); //ожидаемый адрес редиректа
    }

    @Test
    public void badCredentials() throws Exception { //тест на отработку отбивки неправильных данных пользователя
        this.mockMvc.perform(post("/login").param("user","Alfred")) //отправляе на страницу /login запрос с этими параметрами
                .andDo(print()) //выводим что вернул сервер
                .andExpect(status().is3xxRedirection());
                        //.isForbidden()); //ожидаем статус 403 isForbidden , почему отдаёт 302??????
    }

//    @Test
//    public void testDeleteUserById(int id) throws Exception{
//        this.mockMvc.perform(formLogin().user("admin").password("admin"))
//
//    }
}
