package de.unileipzig.bis.rbs.testApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * A simple controller for a restricted area
 *
 * @author Stephan Kemper
 */
@Controller
public class RestrictedController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RestrictedController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Restricted area
     *
     * @return the restricted view
     */
    @RequestMapping("/restricted")
    public String restricted(Model model) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM rbs_users");
        System.out.println(result);
        return "restricted";
    }

}