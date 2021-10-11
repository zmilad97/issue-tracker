package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.exception.UserAlreadyExistException;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.HomeService;
import com.github.zmilad97.bugtracker.service.ProjectService;
import com.github.zmilad97.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class HomeController {
    private final UserService userService;
    private final HomeService homeService;
    private final ProjectService projectService;

    @Autowired
    public HomeController(UserService userService, HomeService homeService, ProjectService projectService) {
        this.userService = userService;
        this.homeService = homeService;
        this.projectService = projectService;
    }

    @GetMapping("")
    public ModelAndView index() {
        return new ModelAndView("home/index");
    }

    @GetMapping("login")
    public String login() {
        return "home/login";
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("home/dashboard");
        modelAndView.addObject("statistics", homeService.getStatistics());
        modelAndView.addObject("sideBarProjects",
                projectService.getProjectByUserParticipated(SecurityUtil.getCurrentUser()));
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView("home/signup");
        modelAndView.addObject("user", new UserDto());
        return modelAndView;
    }


    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, HttpServletRequest request, Errors errors, BindingResult result) {
        if (result.hasErrors()) {
            return "home/signup";
        }
        try {
            userService.registerNewUserAccount(userDto);
            return "redirect:/dashboard";
        } catch (UserAlreadyExistException e) { //TODO : fix here to return error
            return "redirect:/signup";
        }
    }
}
