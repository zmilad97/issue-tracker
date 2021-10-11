package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.TeamDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.ProjectService;
import com.github.zmilad97.bugtracker.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class TeamController {
    private final TeamService teamService;
    private final ProjectService projectService;

    public TeamController(TeamService teamService, ProjectService projectService) {
        this.teamService = teamService;
        this.projectService = projectService;
    }

    @GetMapping("teams")
    public ModelAndView teams() {
        ModelAndView modelAndView = new ModelAndView("team/teams");
        modelAndView.addObject("teams", teamService.getTeamDtoByCreator(SecurityUtil.getCurrentUser().getId()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }


    @GetMapping("/teams/im-in")
    public ModelAndView teamsImIn() {
        ModelAndView modelAndView = new ModelAndView("team/teams-im-in");
        modelAndView.addObject("teams", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/team/create")
    public ModelAndView createTeam() {
        ModelAndView modelAndView = new ModelAndView("team/create-team");
        modelAndView.addObject("team", new TeamDto());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
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
        ModelAndView modelAndView = new ModelAndView("team/edit-team");
        modelAndView.addObject("team", teamService.getTeamDtoByTeamId(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/team/{id}/delete")
    public RedirectView deleteTeam(@PathVariable int id) {
        teamService.deleteTeam(id);
        return new RedirectView("/teams");
    }

}
