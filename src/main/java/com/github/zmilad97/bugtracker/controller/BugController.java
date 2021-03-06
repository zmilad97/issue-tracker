package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.BugService;
import com.github.zmilad97.bugtracker.service.ProjectService;
import com.github.zmilad97.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class BugController {
    private final BugService bugService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public BugController(BugService bugService, ProjectService projectService, UserService userService) {
        this.bugService = bugService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/bugs")
    public ModelAndView bugs() {
        ModelAndView modelAndView = new ModelAndView("bug/bugs");
        List<BugDto> bugs = bugService.retrieveBugDtosByUserCreated(SecurityUtil.getCurrentUser());
        modelAndView.addObject("bugs", bugs);
        modelAndView.addObject("projects", bugService.getProjectDtoSetFromBugDto(bugs));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/bugs/project/{projectId}")
    public ModelAndView projectBugs(@PathVariable int projectId) {
        ModelAndView modelAndView = new ModelAndView("bug/project-bugs");
        modelAndView.addObject("bugs", bugService.getBugDtosByProjectId(projectId));
        modelAndView.addObject("project", projectService.getDtoById(projectId));
        modelAndView.addObject("user", userService.getUserDtoByUser(SecurityUtil.getCurrentUser()));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/bugs/assigned/{projectId}")
    public ModelAndView assignedToMeByProject(@PathVariable int projectId) { //TODO : fix here
        ModelAndView modelAndView = new ModelAndView("assign/assigned-to-me");
        modelAndView.addObject("bugs", bugService.assignedToMeByProject(projectId));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}")
    public ModelAndView getBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("bug/bug");
        modelAndView.addObject("bug", bugService.getBug(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/create")
    public ModelAndView createBug() {
        ModelAndView modelAndView = new ModelAndView("bug/create-bug");
        List<ProjectDto> projects = projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser());
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("bug", new BugDto());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}/assign-user")
    public ModelAndView assignUserToBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("assign/assign-user-to-bug");
        modelAndView.addObject("users", bugService.getUserDtosInBugTeamByBugId(id));
        modelAndView.addObject("bug", bugService.getBugDto(bugService.getBug(id)));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("bug/{id}/save-assign/{userId}")
    public RedirectView saveAssign(@PathVariable int id, @PathVariable int userId) {
        bugService.saveAssign(id, userId);
        return new RedirectView("/bugs");
    }

    @GetMapping("bug/{id}/assign-to-me")
    public RedirectView assignToMe(@PathVariable int id) {
        bugService.assignToMe(id);
        int pid = bugService.getBug(id).getProject().getId();
        return new RedirectView("/bugs/project/" + pid);
    }


    @GetMapping("/bug/{id}/assigned/details")
    public ModelAndView assignedDetails(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("assign/assigned-bug-details"); //TODO: it can be change to simple bug details page
        modelAndView.addObject("bug", bugService.getAssignedBug(id));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }


    @PostMapping("bug/save")
    public String saveBug(@ModelAttribute("bug") @Valid BugDto bugDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bug", bugDto);
            return "bug/create-bug";
        }
        bugDto.setCreatorId(SecurityUtil.getCurrentUser().getId());
        bugService.save(bugDto);
        return "redirect:/bugs";
    }

    @GetMapping("bug/{id}/edit")
    public ModelAndView editBug(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("bug/edit-bug");
        List<ProjectDto> projects = projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser());
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("bug", bugService.getBugDto(bugService.getBug(id)));
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }


    @PostMapping("/bug/update-bug")
    public String updateBugs(@ModelAttribute("bug") @Valid BugDto bugDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<ProjectDto> projects = projectService.getProjectDtoByUserParticipated(SecurityUtil.getCurrentUser());
            model.addAttribute("projects", projects);
            model.addAttribute("bug", bugDto);
            return "bug/edit-bug";
        }
        bugService.update(bugDto);
        return "redirect:/bugs";
    }

    @GetMapping("bug/{id}/delete")
    public RedirectView deleteBug(@PathVariable int id) {
        bugService.deleteBug(bugService.getBug(id));
        return new RedirectView("/bugs");
    }

    @GetMapping("/bug/{id}/status/{status}")
    public RedirectView setStatus(@PathVariable int id, @PathVariable String status, @RequestParam String pId) {
        bugService.setStatus(id, status);
        if (pId != null)
            return new RedirectView("/bugs/assigned/" + pId);
        else
            return new RedirectView("/bugs");
    }

}
