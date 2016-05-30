package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.RoleObjectConsistencyException;
import de.unileipzig.bis.rbs.testApp.model.DataObject;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.model.RoleObject;
import de.unileipzig.bis.rbs.testApp.model.User;
import de.unileipzig.bis.rbs.testApp.service.DataObjectRepository;
import de.unileipzig.bis.rbs.testApp.service.RoleRepository;
import de.unileipzig.bis.rbs.testApp.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.*;

/**
 * The abstract controller as superclass for all other controllers.
 * Child controller need to be annotated with '@Controller'.
 *
 * @author Stephan Kemper
 */
public abstract class AbstractController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected DataObjectRepository dataObjectRepository;

    private HintMessage hintMessage;

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

    private void setRoleObjectRight(RoleObject roleObject, String rightString) {
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
    }

    protected void setRoleObjectsToRole(Role role, Long[] canReadObjectIds, Long[] canWriteObjectIds, Long[] canDeleteObjectIds) throws RoleObjectConsistencyException {
        HashMap<String, Long[]> idCollection = new HashMap<>();
        idCollection.put("can_read", canReadObjectIds);
        idCollection.put("can_write", canWriteObjectIds);
        idCollection.put("can_delete", canDeleteObjectIds);
        HashMap<Long, RoleObject> roleObjects = new HashMap<>();
        for (Map.Entry<String, Long[]> entry: idCollection.entrySet()) {
            String rightString = entry.getKey();
            Long[] objectIds = entry.getValue();
            if (objectIds != null) {
                for (Long objectId : objectIds) {
                    RoleObject roleObject = roleObjects.get(objectId);
                    if (roleObject == null) {
                        roleObject = new RoleObject();
                        roleObject.setRole(role);
                        roleObject.setObject(dataObjectRepository.findOne(objectId));
                    }
                    setRoleObjectRight(roleObject, rightString);
                    checkRoleObjectConsistency(roleObject);
                    roleObjects.put(objectId, roleObject);
                }
            }
        }
        role.getRoleObjects().clear();
        role.getRoleObjects().addAll(new HashSet<>(roleObjects.values()));
    }

    protected void setRoleObjectsToObject(DataObject object, Long[] canReadRoleIds, Long[] canWriteRoleIds, Long[] canDeleteRoleIds) throws RoleObjectConsistencyException {
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
                    setRoleObjectRight(roleObject, rightString);
                    checkRoleObjectConsistency(roleObject);
                    roleObjects.put(roleId, roleObject);
                }
            }
        }
        object.getRoleObjects().clear();
        object.getRoleObjects().addAll(new HashSet<>(roleObjects.values()));
    }

    private void checkRoleObjectConsistency(RoleObject roleObject) throws RoleObjectConsistencyException {
        Role role = roleObject.getRole();
        Set<Role> ascendants = role.findAscendants();
        for (Role r: ascendants) {
            if (roleHasMoreRightsThanAscendant(r, role, roleObject.getObject())) {
                throw new RoleObjectConsistencyException();
            }
        }
    }

    private boolean roleHasMoreRightsThanAscendant(Role ascendant, Role role, DataObject object) {
        return false;
    }

    @ModelAttribute("hintMessage")
    public HintMessage getHintMessage() {
        HintMessage message = hintMessage;
        hintMessage = null;
        return message;
    }

    protected void setHintMessage(HintMessage hintMessage) {
        this.hintMessage = hintMessage;
    }

    public boolean canRead(Long objectId) {
        return canRead(dataObjectRepository.findOne(objectId));
    }

    public boolean canWrite(Long objectId) {
        return canWrite(dataObjectRepository.findOne(objectId));
    }

    public boolean canDelete(Long objectId) {
        return canDelete(dataObjectRepository.findOne(objectId));
    }

    public boolean canRead(DataObject object) {
        return getCurrentUser().canRead(object);
    }

    public boolean canWrite(DataObject object) {
        return getCurrentUser().canWrite(object);
    }

    public boolean canDelete(DataObject object) {
        return getCurrentUser().canDelete(object);
    }

}
