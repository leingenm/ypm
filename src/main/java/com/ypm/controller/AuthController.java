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
        return getModelAndView("success", "Login Success",
            "Welcome back! You have successfully logged in.");
    }

    @GetMapping("/error")
    public ModelAndView error() {
        return getModelAndView("error", "Login Error",
            "An error occurred while logging in. Please try again.");
    }

    private ModelAndView getModelAndView(String type, String title, String message) {
        ModelAndView modelAndView = new ModelAndView("auth");
        modelAndView.addObject("type", type);
        modelAndView.addObject("title", title);
        modelAndView.addObject("message", message);

        return modelAndView;
    }
}
