package com.sam.dataviewer.adminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    @GetMapping("/admin")
    public String hello() {
        return "admin/index";
    }

    @GetMapping("/admin/about")
    public String about() { return "admin/about";}
}
