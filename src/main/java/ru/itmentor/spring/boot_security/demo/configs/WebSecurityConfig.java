package ru.itmentor.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // .csrf().disable()//Включаем Для постмана

                .authorizeRequests()
                .antMatchers("/","/index","/hello").permitAll()

               .antMatchers("/admin/**").hasRole("ADMIN")  //Отключаем для POSTMANа
                .antMatchers("/user/**").hasAnyRole("USER","ROLE_USER","ROLE_ADMIN", "ADMIN") //отключаем для постмана
                .and()
                .formLogin()
               .successHandler(successUserHandler)    //отключаем для POSTMANа
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() { //настройка шифрования паролей
        return new BCryptPasswordEncoder(10);
    }
}