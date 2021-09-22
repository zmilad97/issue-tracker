package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.BugService;
import com.github.zmilad97.bugtracker.service.ProjectService;
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
    private final ProjectService projectService;

    @Autowired
    public BugController(BugService bugService, ProjectService projectService) {
        this.bugService = bugService;
        this.projectService = projectService;
    }

    @GetMapping("/bugs")
    public ModelAndView bugs() {
        ModelAndView modelAndView = new ModelAndView("/bug/bugs");
        modelAndView.addObject("bugs", bugService.retrieveBugs());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/bugs/project/{projectId}")
    public ModelAndView projectBugs(@PathVariable int projectId) {
        ModelAndView modelAndView = new ModelAndView("/bug/project-bugs");
        modelAndView.addObject("bugs", bugService.getBugDtosByProjectId(projectId));
        modelAndView.addObject("project", projectService.getDtoById(projectId));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}")
    public ModelAndView getBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/bug/bug");
        modelAndView.addObject("bug", bugService.getBug(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/create")
    public ModelAndView createBug() {
        ModelAndView modelAndView = new ModelAndView("/bug/create-bug");
        List<ProjectDto> projects = projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser());
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("bug", new BugDto());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}/assign-user")
    public ModelAndView assignUserToBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/assign/assign-user-to-bug");
        modelAndView.addObject("users", bugService.assignUser(id));
        modelAndView.addObject("bug", bugService.getBugDto(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}/save-assign/{userId}")
    public RedirectView saveAssign(@PathVariable int id, @PathVariable int userId) {
        bugService.saveAssign(id, userId);
        return new RedirectView("/bugs");
    }


    @GetMapping("/bugs/assigned-to-me")
    public ModelAndView assignedToMe() {
        ModelAndView modelAndView = new ModelAndView("/assign/assigned-to-me");
        modelAndView.addObject("bugs", bugService.assignedToMe());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/bug/{id}/assigned/details")
    public ModelAndView assignedDetails(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/assign/assigned-bug-details");
        modelAndView.addObject("bug", bugService.getAssignedBug(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }


    @PostMapping("bug/save")
    public RedirectView saveBug(@ModelAttribute("bug") BugDto bugDto) {
        bugService.save(bugDto);
        return new RedirectView("/bugs");
    }

    @GetMapping("bug/{id}/edit")
    public ModelAndView editBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("/bug/edit-bug");
        List<ProjectDto> projects = projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser());
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("bug", bugService.getBug(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
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

    @GetMapping("/bug/{id}/completed/{condition}")
    public RedirectView markCompleted(@PathVariable int id, @PathVariable String condition) {
        bugService.complete(id,condition);
        return new RedirectView("/bugs/assigned-to-me");
    }
}
