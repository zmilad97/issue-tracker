package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.ProjectService;
import com.github.zmilad97.bugtracker.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final TeamService teamService;

    @Autowired
    public ProjectController(ProjectService projectService, TeamService teamService) {
        this.projectService = projectService;
        this.teamService = teamService;
    }

    @GetMapping("/projects")
    public ModelAndView allProjects() {
        ModelAndView modelAndView = new ModelAndView("project/projects");
        modelAndView.addObject("projects",
                projectService.getProjectDtosCreatedByUser(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/project/add")
    public ModelAndView addProject() {
        ModelAndView modelAndView = new ModelAndView("project/create-project");
        modelAndView.addObject("project", new ProjectDto());
        modelAndView.addObject("teams", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @PostMapping("/project/save")
    public RedirectView saveProject(@ModelAttribute("project") @Valid ProjectDto projectDto) {
        projectService.addProject(projectDto);
        return new RedirectView("/projects");
    }

    @GetMapping("/project/{id}/edit")
    public ModelAndView editProject(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("project/edit-project");
        modelAndView.addObject("project", projectService.getDtoById(id));
        modelAndView.addObject("teams", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @PostMapping("/project/update")
    public RedirectView updateProject(@ModelAttribute("project") ProjectDto projectDto) {
        projectService.updateProject(projectDto);
        return new RedirectView("/projects");
    }

    @GetMapping("/projects/participated")
    public ModelAndView myProjects() {
        ModelAndView modelAndView = new ModelAndView("project/projects-participated");
        modelAndView.addObject("projects",
                projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/project/{id}")
    public ModelAndView projectDetails(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("project/project-details");
        modelAndView.addObject("project", projectService.getDtoById(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }


}
