package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    RoleRepository roleRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }


    public User findUserById(int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(null);
    }


    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean saveUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername()); //ищем в БД пользователя с таким именем

        if (userFromDb != null) { //заканчиваем работу если такой уже есть
            return false;
        }
        //Если имя пользователя не занято, добавляем пользователя с ролью юзер
        user.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));//пароль шифруем енкодером
        //сохраняем нового юзера в БД
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean deleteUser(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    public List<User> userGetList(Integer minId) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id>:ID", User.class)
                .setParameter("ID", minId).getResultList();
    }


    @Transactional
    public void update(int id, User updateUser) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(updateUser.getUsername());
            user.setAge(updateUser.getAge());
            user.setEmail(updateUser.getEmail());
            user.setPassword(updateUser.getPassword());
            userRepository.save(user);

//        updateUser.setId(id);
//        userRepository.save(updateUser);

//
//        User user = userRepository.findById((long) id);
//        user.setUsername(updateUser.getUsername());
//        user.setAge(updateUser.getAge());
//        user.setEmail(updateUser.getEmail());
//        user.setPassword(updateUser.getPassword());
        }
    }
}

