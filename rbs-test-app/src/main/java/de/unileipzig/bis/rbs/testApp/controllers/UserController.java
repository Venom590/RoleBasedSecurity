package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String users(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/all-users";
    };

    @RequestMapping(value = "/user/{userid}", method = RequestMethod.GET)
    public String user(@PathVariable String userid, Model model) {
        User user = userRepository.findOne(Long.valueOf(userid));
        model.addAttribute("user", user);
        return "user/user";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public String create() {
        return "user/create";
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "name", required = false) String name) {
        userRepository.save(new User(username, password, name));
        return "redirect:/user";
    }

    @RequestMapping(value = "/user/edit/{userid}", method = RequestMethod.GET)
    public String edit(@PathVariable String userid, Model model) {
        User user = userRepository.findOne(Long.valueOf(userid));
        model.addAttribute("user", user);
        return "user/edit";
    }

    @RequestMapping(value = "/user/edit/{userid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String userid,
                         @RequestParam(value = "username") String username,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "name", required = false) String name) {
        User user = userRepository.findOne(Long.valueOf(userid));
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        userRepository.save(user);
        return "redirect:/user/" + userid;
    }

    @RequestMapping(value = "/user/delete/{userid}", method = RequestMethod.GET)
    public String delete(@PathVariable String userid) {
        userRepository.delete(Long.valueOf(userid));
        return "redirect:/user";
    }

}