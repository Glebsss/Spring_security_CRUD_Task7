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

@RunWith(SpringRunner.class) //��������� ��������� � �������� ����� ���������� �����
@SpringBootTest
//��������� ��� ��������� ����� � ��������� ������ ��� ����������(������������� ����� � ����� � ����� ��� ����������� �������������)
@AutoConfigureMockMvc
//��������� ������� ��������� ������� ������� �������� MVC. ��� ��� ����� ������� ����� ������������ ����������
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminController helloController;

    @Test //�������� �������� ������
    public void test() throws Exception {
//        assertThat(adminController).isNotNull(); //��������� ��� ���������� � ���������� ��������
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome to Index PAGE!")));
    }

    @Test //�������� �������� ������
    public void sayHelloTest() throws Exception {
//        assertThat(adminController).isNotNull(); //��������� ��� ���������� � ���������� ��������
        this.mockMvc.perform(get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello world!")));
    }

    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andDo(print()) //������� ���������
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null)); //������� ��� ������� �������� ����� ������ ?
    }

    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("admin"))   //��������� ����������� ������������ ����� MockMvc
                .andDo(print()) //������� ���������
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    public void badCredentials() throws Exception { //���� �� ��������� ������� ������������ ������ ������������
        this.mockMvc.perform(post("/login").param("user","Alfred")) //��������� �� �������� /login ������ � ����� �����������
                .andDo(print()) //������� ��� ������ ������
                .andExpect(status().is3xxRedirection());
                        //.isForbidden()); //������� ������ 403 isForbidden , ������ ����� 302??????
    }

//    @Test
//    public void testDeleteUserById(int id) throws Exception{
//        this.mockMvc.perform(formLogin().user("admin").password("admin"))
//
//    }
}
