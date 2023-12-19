package com.ClubManagementSystem.web.controller;

import com.ClubManagementSystem.web.dto.RegistrationDto;
import com.ClubManagementSystem.web.models.UserEntity;
import com.ClubManagementSystem.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {
    private UserService userService;
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String sender;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user")RegistrationDto user,
                           BindingResult result, Model model) {
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        UserEntity existingUserUsername = userService.findByUsername(user.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);

            return "register";
        }
        userService.saveUser(user);
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText("Thank you for signup, welcome to our system " + user.getUsername() + "😉");
        mailMessage.setSubject("Thank you Signup *.*");

        javaMailSender.send(mailMessage);
        return "redirect:/clubs?success";
    }
}
