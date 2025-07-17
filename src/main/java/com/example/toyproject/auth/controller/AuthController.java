package com.example.toyproject.auth.controller;

import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        System.out.println("Principal: " + authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            // 인증 객체의 principal을 직접 확인
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                System.out.println("name: " + user.getName());
                String name = user.getName();
                String email = user.getEmail();

                model.addAttribute("name", name);
                model.addAttribute("email", email);
                model.addAttribute("logged", true);
            } else {
                // 디버깅을 위해 principal 타입 확인
                System.out.println("Principal type: " + (principal != null ? principal.getClass().getName() : "null"));
                model.addAttribute("logged", false);
            }
        } else {
            model.addAttribute("logged", false);
        }

        return "index";
    }
}