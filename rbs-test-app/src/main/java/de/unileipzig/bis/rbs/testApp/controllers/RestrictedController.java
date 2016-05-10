package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestrictedController {

    @RequestMapping("/restricted")
    public String login() {
        return "restricted";
    }

}