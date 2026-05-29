package com.example.bugtracker.controller;

import com.example.bugtracker.entity.Bug;
import com.example.bugtracker.entity.User;
import com.example.bugtracker.entity.Version;
import com.example.bugtracker.entity.enums.Priority;
import com.example.bugtracker.service.BugService;
import com.example.bugtracker.service.UserService;
import com.example.bugtracker.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bugs")
@RequiredArgsConstructor
public class BugController {
    private final BugService bugService;
    private final VersionService versionService;
    private final UserService userService;

    @GetMapping
    public String listBugs(Model model) {
        model.addAttribute("bugs", bugService.getAllBugs());
        return "bugs";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'TESTER')")
    public String createForm(Model model) {
        model.addAttribute("bug", new Bug());
        model.addAttribute("versions", versionService.getAllVersions());
        model.addAttribute("priorities", Priority.values());
        return "bug-form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER', 'TESTER')")
    public String createBug(@ModelAttribute Bug bug,
                            @RequestParam("versionId") Long versionId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        User reporter = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Version version = versionService.findById(versionId);
        bug.setVersion(version);
        bug.setReportedBy(reporter);
        bugService.createBug(bug);
        return "redirect:/bugs";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public String editForm(@PathVariable Long id, Model model) {
        Bug bug = bugService.findById(id);
        model.addAttribute("bug", bug);
        model.addAttribute("versions", versionService.getAllVersions());
        model.addAttribute("priorities", Priority.values());
        return "bug-form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    public String updateBug(@PathVariable Long id, @ModelAttribute Bug bug,
                            @RequestParam("versionId") Long versionId) {
        Version version = versionService.findById(versionId);
        bug.setId(id);
        bug.setVersion(version);
        bugService.updateBug(bug);
        return "redirect:/bugs";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBug(@PathVariable Long id) {
        bugService.deleteBug(id);
        return "redirect:/bugs";
    }
}