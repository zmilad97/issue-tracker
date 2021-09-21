package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.exception.UserAlreadyExistException;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import com.github.zmilad97.bugtracker.service.HomeService;
import com.github.zmilad97.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class HomeController {
    private final UserService userService;
    private final HomeService homeService;
    @Autowired
    public HomeController(UserService userService, HomeService homeService) {
        this.userService = userService;
        this.homeService = homeService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/home/index");
        UserDto userDto = new UserDto();
        userDto.setFirstName(SecurityUtil.getCurrentUser().getFirstName());
        modelAndView.addObject("user", userDto);
        return modelAndView;
    }

    @GetMapping("login")
    public String login() {
        return "/home/login";
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("/home/dashboard");
        modelAndView.addObject("statistics" , homeService.getStatistics());
        return modelAndView;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView("/home/signup");
        modelAndView.addObject("user", new UserDto());
        return modelAndView;
    }


    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("user") @Valid UserDto userDto, HttpServletRequest request, Errors errors) {
        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/home/index", "user", userDto);
    }
}
