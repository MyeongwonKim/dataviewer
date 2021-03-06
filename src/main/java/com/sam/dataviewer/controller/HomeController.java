package com.sam.dataviewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String hello() {
        return "index";
    }

    @GetMapping("/about")
    public String about() { return "about";}

    @GetMapping("/payment")
    public String payment() { return "payment";}
}
