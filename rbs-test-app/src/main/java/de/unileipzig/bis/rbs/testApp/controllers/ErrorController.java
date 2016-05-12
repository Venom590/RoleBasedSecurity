package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

    @RequestMapping(value = "403", method = RequestMethod.GET)
    public String accessDenied() {
        return "error/403";
    }

}
