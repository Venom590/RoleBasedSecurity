package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A simple controller for a restricted area
 *
 * @author Stephan Kemper
 */
@Controller
public class RestrictedController {

    /**
     * Restricted area
     *
     * @return the restricted view
     */
    @RequestMapping("/restricted")
    public String restricted() {
        return "restricted";
    }

}