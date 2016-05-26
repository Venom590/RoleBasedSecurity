package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.DataObject;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.service.DataObjectRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

/**
 * The object controller to manage objects in this application.
 *
 * @author Stephan Kemper
 */
@Controller
@RequestMapping("/manage/object")
public class DataObjectController extends AbstractController {

    /**
     * The object repository to persist changes
     */
    @Autowired
    private DataObjectRepository dataObjectRepository;

    /**
     * The role repository
     */
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get all objects
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String objects(Model model) {
        Iterable<DataObject> objects = dataObjectRepository.findAll();
        model.addAttribute("objects", objects);
        return "object/all-objects";
    };

    /**
     * Get object by id
     *
     * @param objectid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{objectid}", method = RequestMethod.GET)
    public String object(@PathVariable String objectid, Model model) {
        DataObject object = dataObjectRepository.findOne(Long.valueOf(objectid));
        model.addAttribute("object", object);
        return "object/object";
    }

    /**
     * Create new object (show mask)
     *
     * @param model the ui model
     * @return the object creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "object/create";
    }

    /**
     * Create new object (action)
     *
     * @param name the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "roles[]", required = false) Long[] roleIds) {
        DataObject object = new DataObject(name);
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findOne(roleId));
            }
        }
        object.setRoles(roles);
        dataObjectRepository.save(object);
        return "redirect:/manage/object";
    }

    /**
     * Edit existing object (show mask)
     *
     * @param objectid the object id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{objectid}", method = RequestMethod.GET)
    public String edit(@PathVariable String objectid, Model model) {
        DataObject object = dataObjectRepository.findOne(Long.valueOf(objectid));
        model.addAttribute("object", object);
        Iterable<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "object/edit";
    }

    /**
     * Edit existing object (action)
     *
     * @param objectid the object id
     * @param name the new name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{objectid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String objectid,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "roles[]", required = false) Long[] roleIds) {
        DataObject object = dataObjectRepository.findOne(Long.valueOf(objectid));
        object.setName(name);
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(roleRepository.findOne(roleId));
            }
        }
        object.setRoles(roles);
        dataObjectRepository.save(object);
        return "redirect:/manage/object/" + objectid;
    }

    /**
     * Delete existing object
     *
     * @param objectid the object id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{objectid}", method = RequestMethod.GET)
    public String delete(@PathVariable String objectid) {
        dataObjectRepository.delete(Long.valueOf(objectid));
        return "redirect:/manage/object";
    }

}