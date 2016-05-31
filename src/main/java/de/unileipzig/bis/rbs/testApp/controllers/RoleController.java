package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.RoleObjectConsistencyException;
import de.unileipzig.bis.rbs.testApp.model.DataObject;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.DataObjectRepository;
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
 * The role controller to manage roles in this application.
 *
 * @author Stephan Kemper
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/manage/role")
public class RoleController extends AbstractController {

    /**
     * The object repository
     */
    @Autowired
    private DataObjectRepository dataObjectRepository;

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
        Iterable<User> users = userRepository.findAll();
        Iterable<DataObject> objects = dataObjectRepository.findAll();
        model.addAttribute("roles", allRoles);
        model.addAttribute("users", users);
        model.addAttribute("objects", objects);
        return "role/create";
    }

    /**
     * Create new role (action)
     *
     * @param parentId the parent id
     * @param name the name
     * @param userIds the user ids to set
     * @param canReadObjectIds the object ids to set canRead
     * @param canWriteObjectIds the object ids to set canWrite
     * @param canDeleteObjectIds the object ids to set canDelete
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "parent_id") Long parentId,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "users[]", required = false) Long[] userIds,
                           @RequestParam(value = "can_read[]", required = false) Long[] canReadObjectIds,
                           @RequestParam(value = "can_write[]", required = false) Long[] canWriteObjectIds,
                           @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteObjectIds) {
        Role role = new Role();
        role.setName(name);
        if (parentId != null && parentId != 0) {
            role.setParentRole(roleRepository.findOne(parentId));
        }
        Set<User> users = new HashSet<>();
        if (userIds != null) {
            for (Long userId : userIds) {
                users.add(userRepository.findOne(userId));
            }
        }
        role.getUsers().addAll(users);
        try {
            setRoleObjectsToRole(role, canReadObjectIds, canWriteObjectIds, canDeleteObjectIds);
            roleRepository.save(role);
        } catch (RoleObjectConsistencyException e) {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
        }
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
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        Iterable<DataObject> objects = dataObjectRepository.findAll();
        model.addAttribute("objects", objects);
        return "role/edit";
    }

    /**
     * Edit existing role (action)
     *
     * @param roleid the role id
     * @param parentId the new parent id
     * @param name the new name
     * @param userIds the user ids to set
     * @param canReadObjectIds the object ids to set canRead
     * @param canWriteObjectIds the object ids to set canWrite
     * @param canDeleteObjectIds the object ids to set canDelete
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{roleid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String roleid,
                         @RequestParam(value = "parent_id", required = false) Long parentId,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "users[]", required = false) Long[] userIds,
                         @RequestParam(value = "can_read[]", required = false) Long[] canReadObjectIds,
                         @RequestParam(value = "can_write[]", required = false) Long[] canWriteObjectIds,
                         @RequestParam(value = "can_delete[]", required = false) Long[] canDeleteObjectIds) {
        Role role = roleRepository.findOne(Long.valueOf(roleid));
        if (parentId != null && parentId != 0) {
            Role parentRole = roleRepository.findOne(parentId);
            if (parentRole.findAscendants().contains(role)) {
                setHintMessage(new HintMessage(HintMessage.HintStatus.danger, "You can not set a parent role which has this role in its ascendants."));
                return "redirect:/manage/role/edit/" + roleid;
            }
            role.setParentRole(parentRole);
        } else {
            role.setParentRole(null);
        }
        role.setName(name);
        Set<User> users = new HashSet<>();
        if (userIds != null) {
            for (Long userId : userIds) {
                users.add(userRepository.findOne(userId));
            }
        }
        role.getUsers().clear();
        role.getUsers().addAll(users);
        try {
            setRoleObjectsToRole(role, canReadObjectIds, canWriteObjectIds, canDeleteObjectIds);
            roleRepository.save(role);
        } catch (RoleObjectConsistencyException e) {
            setHintMessage(new HintMessage(HintMessage.HintStatus.danger, e.getMessage()));
        }
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