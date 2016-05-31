package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simple error controller
 *
 * @author Lukas Werner
 */
@Controller
public class AppErrorController extends AbstractController implements ErrorController {

    /**
     * The error view
     *
     * @return error view
     */
    @RequestMapping(value = "/error")
    public String error() {
        return "error/error";
    }

    /**
     * Handling the access denied error
     *
     * @return the view
     */
    @RequestMapping(value = "error/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "error/403";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
