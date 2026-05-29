package com.example.bugtracker.controller;

import com.example.bugtracker.entity.enums.ReleaseStatus;
import com.example.bugtracker.service.ReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {
    private final ReleaseService releaseService;

    @GetMapping
    public String listReleases(Model model) {
        model.addAttribute("releases", releaseService.getAllReleases());
        return "releases";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createForm(Model model) {
        model.addAttribute("release", null); // для единого шаблона
        model.addAttribute("versions", releaseService.getAllVersions());
        model.addAttribute("stores", releaseService.getAllStores());
        model.addAttribute("statuses", ReleaseStatus.values());
        return "release-form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createRelease(@RequestParam Long versionId,
                                @RequestParam Long storeId,
                                @RequestParam ReleaseStatus status) {
        releaseService.createRelease(versionId, storeId, status);
        return "redirect:/releases";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("release", releaseService.findById(id));
        model.addAttribute("versions", releaseService.getAllVersions());
        model.addAttribute("stores", releaseService.getAllStores());
        model.addAttribute("statuses", ReleaseStatus.values());
        return "release-form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateRelease(@PathVariable Long id,
                                @RequestParam Long versionId,
                                @RequestParam Long storeId,
                                @RequestParam ReleaseStatus status) {
        releaseService.updateRelease(id, versionId, storeId, status);
        return "redirect:/releases";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return "redirect:/releases";
    }
}