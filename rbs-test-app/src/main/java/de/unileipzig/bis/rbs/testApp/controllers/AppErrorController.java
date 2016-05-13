package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple error controller
 *
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/error")
public class AppErrorController extends AbstractController{

    /**
     * Handling the access denied error
     *
     * @return the view
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "error/403";
    }

}
