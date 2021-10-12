package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.ProjectService;
import com.github.zmilad97.bugtracker.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String addProject(Model model) {
        model.addAttribute("project", new ProjectDto());
        model.addAttribute("teams", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));
        model.addAttribute("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return "project/create-project";
    }

    @PostMapping("/project/save")
    public String saveProject(Model model, @Valid @ModelAttribute("project") ProjectDto projectDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("project", projectDto);
            model.addAttribute("team", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));

            return "project/create-project";
        }
        projectService.addProject(projectDto);
        return "redirect:/projects";
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
    public String updateProject(@ModelAttribute("project") @Valid ProjectDto projectDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("project", projectDto);
            model.addAttribute("teams", teamService.getTeamDtoByUser(SecurityUtil.getCurrentUser()));
            return "project/edit-project";
        }
        projectService.updateProject(projectDto);
        return "redirect:/projects";
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
