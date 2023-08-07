package ru.itmentor.spring.boot_security.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmentor.spring.boot_security.demo.RESTcontrollers.AdminRestController;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class) //указываем окружение в которому будут стартовать тесты
@SpringBootTest
//указывает что запускаем тесты в окружении спринг бут приложени€(автоматически пойдЄт в папки и найдЄт все необходимые конфигурациии)
@AutoConfigureMockMvc
//ѕозвол€ет создать структуру классов котора€ подменит MVC. ƒаЄт нам более удобный метод тестировани€ приложени€
@WithUserDetails("admin")
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRestController adminController;

    @Test
    public void adminIdTest() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sessionId = authentication.getName();
        this.mockMvc.perform(get("/admin/rest/1"))
                .andDo(print())
                .andExpect(authenticated()) //(провер€ет корректно ли атуентифицирован пользователь)пройдЄт проверку только если в контексте дл€ текущего пользвател€ установленна веб-сесси€
                //добавл€ем аннотацию WithUserDetails, туда передаем им€ пользовател€ под которым хотим выполн€ть данные тесты
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(sessionId))
                .andExpect(jsonPath("$.id").value(1));
    }

}
