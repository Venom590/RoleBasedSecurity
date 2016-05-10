package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The basic home controller and main entry point
 *
 * @author Lukas Werner
 */
@Controller
public class HomeController {

    /**
     * Main entry point (home)
     *
     * @return the view name
     */
    @RequestMapping("/")
    public String home() {
        return "home";
    }

}