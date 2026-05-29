package com.example.bugtracker.controller;

import com.example.bugtracker.entity.Version;
import com.example.bugtracker.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/versions")
@RequiredArgsConstructor
public class VersionController {
    private final VersionService versionService;

    @GetMapping
    public String listVersions(Model model) {
        model.addAttribute("versions", versionService.getAllVersions());
        return "versions";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createForm(Model model) {
        model.addAttribute("version", new Version());
        model.addAttribute("applications", versionService.getAllApplications());
        return "version-form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createVersion(@ModelAttribute Version version, @RequestParam Long appId) {
        versionService.createVersion(version, appId);
        return "redirect:/versions";
    }
}