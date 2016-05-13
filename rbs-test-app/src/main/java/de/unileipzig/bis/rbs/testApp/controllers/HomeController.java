package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The basic home controller and main entry point
 *
 * @author Lukas Werner
 */
@Controller
public class HomeController extends AbstractController{

    @Autowired
    private UserRepository userRepository;

    /**
     * Main entry point (home)
     *
     * @return the view name
     */
    @RequestMapping("/")
    public String home(Model model) {
        String username = this.getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        String name = "Guest";
        if (user != null) {
            name = user.getName();
        }
        model.addAttribute("name", name);
        return "home";
    }

}