package com.github.sakumas.healthtracker.controller;

import com.github.sakumas.healthtracker.entity.User;
import com.github.sakumas.healthtracker.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {


        if (!userService.register(user, request, response)) {
            model.addAttribute("error", "このユーザー名はすでに使用されています");
            return "user/register";
        }

        return "redirect:/records";
    }
}
