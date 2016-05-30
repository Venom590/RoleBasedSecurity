package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.RoleObjectConsistencyException;
import de.unileipzig.bis.rbs.testApp.model.DataObject;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.RoleObject;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.DataObjectRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleObjectRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * The object controller to manage objects in this application.
 *
 * @author Stephan Kemper
 */
@Controller
@RequestMapping("/manage/object")
public class DataObjectController extends AbstractController {

    @Autowired
    private RoleObjectRepository roleObjectRepository;

    /**
     * Get all objects
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String objects(Model model) {
        User user = this.getCurrentUser();
        Iterable<DataObject> allObjects = dataObjectRepository.findAll();
        model.addAttribute("objects", allObjects);
        return "object/all-objects";
    };

    /**
     * Get all objects
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/myobjects", method = RequestMethod.GET)
    public String myObjects(Model model) {
        User user = this.getCurrentUser();
        Iterable<DataObject> allObjects = dataObjectRepository.findAll();
        List<DataObject> objects = new ArrayList<>();
        for (DataObject object: allObjects) {
            if (user.canRead(object)) {
                objects.add(object);
            }
        }
        model.addAttribute("objects", objects);
        return "object/my-objects";
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
        return "redirect:/manage/" + object.getClass().getSimpleName().toLowerCase() + "/" + objectid;
//        model.addAttribute("object", object);
//        return "object/object";
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
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                           @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                           @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        DataObject object = new DataObject();
        try {
            setRoleObjectsToObject(object, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);
            dataObjectRepository.save(object);
        } catch (RoleObjectConsistencyException e) {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
        }
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
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{objectid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String objectid,
                         @RequestParam(value = "can_read[]", required = false) Long[] canReadRoleIds,
                         @RequestParam(value = "can_write[]", required = false) Long[] canWriteRoleIds,
                         @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteRoleIds) {
        DataObject object = dataObjectRepository.findOne(Long.valueOf(objectid));
        try {
            setRoleObjectsToObject(object, canReadRoleIds, canWriteRoleIds, canDeleteRoleIds);
            dataObjectRepository.save(object);
        } catch (RoleObjectConsistencyException e) {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
        }
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