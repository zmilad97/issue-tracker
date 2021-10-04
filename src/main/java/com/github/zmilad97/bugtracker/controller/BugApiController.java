package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.service.BugService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class BugApiController {
    private final BugService bugService;
    //TODO : complete Api

    public BugApiController(BugService bugService) {
        this.bugService = bugService;
    }

    @GetMapping("/bug/{id}")
    public BugDto getBug(@PathVariable int id) {
        return bugService.getBugDto(id); //TODO: make it not to access from other teams
    }


}
