package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The login controller
 *
 * @author Stephan Kemper
 */
@Controller
public class LoginController extends AbstractController{

    /**
     * Login route
     *
     * @return the login view
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

}