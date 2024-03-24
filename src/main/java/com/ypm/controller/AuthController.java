package com.ypm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/success")
    public ModelAndView success() {
        ModelAndView modelAndView = new ModelAndView("auth");
        modelAndView.addObject("type", "success");
        modelAndView.addObject("title", "Login Successful");
        modelAndView.addObject("message",
            "Welcome back! You have successfully logged in.");

        return modelAndView;
    }

    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView modelAndView = new ModelAndView("auth");
        modelAndView.addObject("type", "error");
        modelAndView.addObject("title", "Login Error");
        modelAndView.addObject("message",
            "Sorry, there was an error with your login credentials. Please try again.");

        return modelAndView;
    }
}
