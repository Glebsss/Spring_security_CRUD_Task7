package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }
//------------------------------------------------------------------------------------------------------------
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "adminViews/all";
    }

//------------------------------------------------------------------------------------------------------------
    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new User());

        return "adminViews/registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "adminViews/registration";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "adminViews/registration";
        }

        return "redirect:/admin";
    }
//----------------------------------------------------------------------------------------------------------------------
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "adminViews/show";
    }
//----------------------------------------------------------------------------------------------------------------------
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.findUserById(id));
        return "adminViews/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user")@Valid User user,BindingResult bindingResult,
                         @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "adminViews/edit";

        userService.update(id,user);
        return "redirect:/admin";
    }
//---------------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id){
        userService.deleteUser(id);
        return ("redirect:/admin");
    }

}
