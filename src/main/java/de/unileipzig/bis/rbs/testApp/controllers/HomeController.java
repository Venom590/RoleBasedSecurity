package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * The basic home controller and main entry point
 *
 * @author Lukas Werner
 */
@Controller
public class HomeController extends AbstractController {

    /**
     * Main entry point (home)
     *
     * @param model the ui model
     * @return the view name
     */
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("name", currentName());
        return "home";
    }

    /**
     * Register form
     *
     * @return the view name
     */
    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * Register a new user
     *
     * @param username the username
     * @param password the password
     * @param name the name
     * @return the view name (redirect)
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String doRegister(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "name", required = false) String name) {
        User user = new User(username, password, name);
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("user"))));
        userRepository.save(user);
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(username, password, authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(springUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        setHintMessage(new HintMessage(HintMessage.HintStatus.success, "You've successfully signed up. You can now sign in using your credentials."));
        return "redirect:/";
    }

    /**
     * Settings form
     *
     * @return the view name
     */
    @RequestMapping("/settings")
    public String settings() {
        return "settings";
    }


    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String doSettings(@RequestParam(value = "password") String password, @RequestParam(value = "name") String name) {
        User user = getCurrentUser();
        user.setPassword(password);
        user.setName(name);
        userRepository.save(user);
        setHintMessage(new HintMessage(HintMessage.HintStatus.success, "Successfully updated your information."));
        return "redirect:/settings";
    }

}