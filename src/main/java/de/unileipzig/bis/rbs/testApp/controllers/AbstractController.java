package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.DataObject;
import de.unileipzig.bis.rbs.testApp.model.RoleObject;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The abstract controller as superclass for all other controllers.
 * Child controller need to be annotated with '@Controller'.
 *
 * @author Stephan Kemper
 */
public abstract class AbstractController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private String hintMessage = "";

    /**
     * Obtains the currently authenticated principal, or an authentication request token.
     *
     * @return the current authentication
     */
    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @return true if user is admin
     */
    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) {
            return false;
        }
        return authorities.toString().contains("admin");
    }

    /**
     * @return true if user is logged in
     */
    @ModelAttribute("isLogged")
    public boolean isLogged() {
        Authentication auth = getAuthentication();
        if (auth == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if (authorities == null) {
            return false;
        }
        return !authorities.toString().contains("ROLE_ANONYMOUS");
    }

    @ModelAttribute("getCurrentUser")
    protected User getCurrentUser() {
        if (isLogged()) {
            org.springframework.security.core.userdetails.User user;
            user = (org.springframework.security.core.userdetails.User) this.getAuthentication().getPrincipal();
            return userRepository.findByUsername(user.getUsername());
        }
        return null;
    }

    protected void setRoleObjectsToObject(DataObject object, Long[] canReadRoleIds, Long[] canWriteRoleIds, Long[] canDeleteRoleIds) {
        HashMap<String, Long[]> idCollection = new HashMap<>();
        idCollection.put("can_read", canReadRoleIds);
        idCollection.put("can_write", canWriteRoleIds);
        idCollection.put("can_delete", canDeleteRoleIds);
        HashMap<Long, RoleObject> roleObjects = new HashMap<>();
        for (Map.Entry<String, Long[]> entry: idCollection.entrySet()) {
            String rightString = entry.getKey();
            Long[] roleIds = entry.getValue();
            if (roleIds != null) {
                for (Long roleId : roleIds) {
                    RoleObject roleObject = roleObjects.get(roleId);
                    if (roleObject == null) {
                        roleObject = new RoleObject();
                        roleObject.setObject(object);
                        roleObject.setRole(roleRepository.findOne(roleId));
                    }
                    switch (rightString) {
                        case "can_read":
                            roleObject.setCanRead(true);
                            break;
                        case "can_write":
                            roleObject.setCanWrite(true);
                            break;
                        case "can_delete":
                            roleObject.setCanDelete(true);
                            break;
                    }
                    roleObjects.put(roleId, roleObject);
                }
            }
        }
        object.getRoleObjects().clear();
        object.getRoleObjects().addAll(new HashSet<>(roleObjects.values()));
    }

    @ModelAttribute("hintMessage")
    public String getHintMessage() {
        String message = this.hintMessage;
        this.hintMessage = "";
        return message;
    }

    protected void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }

}
