package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The role controller to manage roles in this application.
 *
 * @author Stephan Kemper
 */
@Controller
@RequestMapping("/manage/role")
public class RoleController extends AbstractController {

    /**
     * The role repository to persist changes
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all roles
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String roles(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "role/all-roles";
    };

    /**
     * Get role by id
     *
     * @param roleid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{roleid}", method = RequestMethod.GET)
    public String role(@PathVariable String roleid, Model model) {
        Role role = roleRepository.findOne(Long.valueOf(roleid));
        model.addAttribute("role", role);
        return "role/role";
    }

    /**
     * Create new role (show mask)
     *
     * @param model the ui model
     * @return the role creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Iterable<Role> allRoles = roleRepository.findAll();
        model.addAttribute("roles", allRoles);
        return "role/create";
    }

    /**
     * Create new role (action)
     *
     * @param parentId the parent id
     * @param name the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "parent_id") Long parentId,
                           @RequestParam(value = "name", required = false) String name) {
        Role role = new Role();
        role.setName(name);
        if (parentId != null && parentId != 0) {
            role.setParentRole(roleRepository.findOne(parentId));
        }
        roleRepository.save(role);
        return "redirect:/manage/role";
    }

    /**
     * Edit existing role (show mask)
     *
     * @param roleid the role id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{roleid}", method = RequestMethod.GET)
    public String edit(@PathVariable String roleid, Model model) {
        Role role = roleRepository.findOne(Long.valueOf(roleid));
        model.addAttribute("role", role);
        Iterable<Role> allRoles = roleRepository.findAll();
        model.addAttribute("roles", allRoles);
        return "role/edit";
    }

    /**
     * Edit existing role (action)
     *
     * @param roleid the role id
     * @param parentId the new parent id
     * @param name the new name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{roleid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String roleid,
                         @RequestParam(value = "parent_id", required = false) Long parentId,
                         @RequestParam(value = "name") String name) {
        Role role = roleRepository.findOne(Long.valueOf(roleid));
        if (parentId != null && parentId != 0) {
            Role parentRole = roleRepository.findOne(parentId);
            role.setParentRole(parentRole);
        } else {
            role.setParentRole(null);
        }
        role.setName(name);
        roleRepository.save(role);
        return "redirect:/manage/role/" + roleid;
    }

    /**
     * Delete existing role
     *
     * @param roleid the role id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{roleid}", method = RequestMethod.GET)
    public String delete(@PathVariable String roleid) {
        roleRepository.delete(Long.valueOf(roleid));
        return "redirect:/manage/role";
    }

}