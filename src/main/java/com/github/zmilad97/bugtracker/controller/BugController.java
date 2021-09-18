package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.dtos.TeamDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.BugService;
import com.github.zmilad97.bugtracker.service.TeamService;
import com.github.zmilad97.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class BugController {
    private final BugService bugService;
    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public BugController(BugService bugService, TeamService teamService, UserService userService) {
        this.bugService = bugService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("/bugs")
    public ModelAndView bugs() {
        ModelAndView modelAndView = new ModelAndView("bugs");
        modelAndView.addObject("bugs", bugService.retrieveBugs());
        return modelAndView;
    }

    @GetMapping("bug/{id}")
    public ModelAndView getBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("bug");
        modelAndView.addObject("bug", bugService.getBug(id));
        return modelAndView;
    }

    @GetMapping("bug/create")
    public ModelAndView createBug() {
        ModelAndView modelAndView = new ModelAndView("create-bug");
        List<TeamDto> teams = teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser());
        modelAndView.addObject("teams", teams);
        modelAndView.addObject("bug", new BugDto());
        return modelAndView;
    }

    @GetMapping("bug/{id}/assign-user")
    public ModelAndView assignUserToBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("assign-user-to-bug");
        modelAndView.addObject("users", bugService.assignUser(id));
        modelAndView.addObject("bug", bugService.getBugDto(id));
        return modelAndView;
    }

    @GetMapping("bug/{id}/save-assign/{userId}")
    public RedirectView saveAssign(@PathVariable int id, @PathVariable int userId) {
        bugService.saveAssign(id, userId);
        return new RedirectView("/bugs");
    }


    @PostMapping("bug/save")
    public RedirectView saveBug(@ModelAttribute("bug") BugDto bugDto) {
        bugService.save(bugDto);
        return new RedirectView("/bugs");
    }

    @GetMapping("bug/{id}/edit")
    public ModelAndView editBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("edit-bug");
        List<TeamDto> teams = teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser());
        modelAndView.addObject("teams", teams);
        modelAndView.addObject("bug", bugService.getBug(id));

        return modelAndView;
    }


    @PostMapping("/bug/update-bug")
    public RedirectView updateBugs(@ModelAttribute("bug") BugDto bugDto) {
        bugService.update(bugDto);
        return new RedirectView("/bugs");
    }

    @GetMapping("bug/{id}/delete")
    public RedirectView deleteBug(@PathVariable int id) {
        bugService.deleteBug(id);
        return new RedirectView("/bugs");
    }

    public ModelAndView markCompleted() {
        return null;
    }
}
