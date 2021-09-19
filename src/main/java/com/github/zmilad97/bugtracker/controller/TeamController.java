package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.TeamDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("teams")
    public ModelAndView teams() {
        ModelAndView modelAndView = new ModelAndView("/team/teams");
        modelAndView.addObject("teams", teamService.getTeamDtoByCreator(SecurityUtil.getCurrentUser().getId()));
        return modelAndView;
    }

    @GetMapping("/team/create")
    public ModelAndView createTeam() {
        ModelAndView modelAndView = new ModelAndView("/team/create-team");
        modelAndView.addObject("team", new TeamDto());
        return modelAndView;
    }

    @PostMapping("/team/save")
    public RedirectView saveTeam(@ModelAttribute("team") TeamDto teamDto) {
        teamService.save(teamDto);
        return new RedirectView("/teams");
    }

    @PostMapping("/team/update-team")
    public RedirectView updateTeam(@ModelAttribute("team") TeamDto teamDto) {
        teamService.saveEdit(teamDto);
        return new RedirectView("/teams");
    }

    @GetMapping("/team/{id}/edit")
    public ModelAndView editTeam(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/team/edit-team");
        modelAndView.addObject("team", teamService.getTeamDtoByTeamId(id));
        return modelAndView;
    }

    @GetMapping("/team/{id}/delete")
    public RedirectView deleteTeam(@PathVariable int id) {
        teamService.deleteTeam(id);
        return new RedirectView("/teams");
    }

}
