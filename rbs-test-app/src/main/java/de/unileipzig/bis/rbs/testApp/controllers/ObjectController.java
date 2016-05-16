package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.Object;
import de.unileipzig.bis.rbs.testApp.service.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The object controller to manage objects in this application.
 *
 * @author Stephan Kemper
 */
@Controller
@RequestMapping("/manage/object")
public class ObjectController extends AbstractController {

    /**
     * The object repository to persist changes
     */
    @Autowired
    private ObjectRepository objectRepository;

    /**
     * Get all objects
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String objects(Model model) {
        Iterable<Object> objects = objectRepository.findAll();
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
        Object object = objectRepository.findOne(Long.valueOf(objectid));
        model.addAttribute("object", object);
        return "object/object";
    }

    /**
     * Create new object (show mask)
     *
     * @return the object creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "object/create";
    }

    /**
     * Create new object (action)
     *
     * @param name the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "name", required = false) String name) {
        objectRepository.save(new Object(name));
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
        Object object = objectRepository.findOne(Long.valueOf(objectid));
        model.addAttribute("object", object);
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
                        @RequestParam(value = "name", required = false) String name) {
        Object object = objectRepository.findOne(Long.valueOf(objectid));
        object.setName(name);
        objectRepository.save(object);
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
        objectRepository.delete(Long.valueOf(objectid));
        return "redirect:/manage/object";
    }

}