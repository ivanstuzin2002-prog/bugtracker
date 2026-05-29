package com.example.bugtracker.controller;

import com.example.bugtracker.entity.enums.RoleName;
import com.example.bugtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("roles", RoleName.values());
        return "user-form";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String lastName,
                             @RequestParam String firstName,
                             @RequestParam(required = false) String patronymic,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam RoleName roleName) {
        userService.createUser(lastName, firstName, patronymic, email, password, roleName);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}