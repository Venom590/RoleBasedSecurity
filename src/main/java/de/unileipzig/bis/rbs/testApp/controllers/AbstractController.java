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
 * Child controller needs to be annotated with '@Controller'.
 *
 * @author Stephan Kemper
 * @author Lukas Werner
 */
public abstract class AbstractController {

    /**
     * The user repo
     */
    @Autowired
    protected UserRepository userRepository;

    /**
     * The role repo
     */
    @Autowired
    protected RoleRepository roleRepository;

    /**
     * The object repo
     */
    @Autowired
    protected DataObjectRepository dataObjectRepository;

    /**
     * The current hint message object
     */
    private HintMessage hintMessage;

    /**
     * Obtains the currently authenticated principal, or an authentication request token.
     *
     * @return the current authentication
     */
    @ModelAttribute("getAuth")
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get the name of the current logged in user
     *
     * @return the name
     */
    @ModelAttribute("currName")
    public String currentName() {
        User user = getCurrentUser();
        if (user == null) {
            return "Guest";
        }
        return user.getName();
    }

    /**
     * Check if the current "visitor" is an admin
     *
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
     * Check if the current "visitor" is logged in
     *
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

    /**
     * Get the current logged in user
     *
     * @return the user if logged in, null elsewise
     */
    @ModelAttribute("getCurrentUser")
    protected User getCurrentUser() {
        if (isLogged()) {
            org.springframework.security.core.userdetails.User user;
            user = (org.springframework.security.core.userdetails.User) this.getAuthentication().getPrincipal();
            return userRepository.findByUsername(user.getUsername());
        }
        return null;
    }

    /**
     * Internal method to reduce code duplicates. It sets the right by a given right string (could be implemented with callbacks)
     *
     * @todo Implement with callbacks
     * @param roleObject the given role object
     * @param rightString the string to decide which right should be set
     */
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

    /**
     * Take a given role and set the rights for the object ids given. Then check the role consistency.
     *
     * @param role the given role
     * @param canReadObjectIds the object ids the object should be allowed to read
     * @param canWriteObjectIds the object ids the object should be allowed to write
     * @param canDeleteObjectIds the object ids the object should be allowed to delete
     * @throws RoleObjectConsistencyException if role consistency rules are broken
     */
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
                    roleObjects.put(objectId, roleObject);
                }
            }
        }
        checkRoleObjectConsistency(roleObjects.values());
        role.getRoleObjects().clear();
        role.getRoleObjects().addAll(new HashSet<>(roleObjects.values()));
    }

    /**
     * Take a given object and set the rights for the role ids given. Then check the role consistency.
     *
     * @param object the given object
     * @param canReadRoleIds the role ids the object should be allowed to read
     * @param canWriteRoleIds the role ids the object should be allowed to write
     * @param canDeleteRoleIds the role ids the object should be allowed to delete
     * @throws RoleObjectConsistencyException if role consistency rules are broken
     */
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
                    roleObjects.put(roleId, roleObject);
                }
            }
        }
        checkRoleObjectConsistency(roleObjects.values());
        object.getRoleObjects().clear();
        object.getRoleObjects().addAll(new HashSet<>(roleObjects.values()));
    }

    /**
     * Check for a given collection of RoleObjects if consistency is preserved
     *
     * @param roleObjects the collection of RoleObjects to check
     * @throws RoleObjectConsistencyException if the consistency rules are broken
     */
    private void checkRoleObjectConsistency(Collection<RoleObject> roleObjects) throws RoleObjectConsistencyException {
        for (RoleObject roleObject: roleObjects) {
            Role role = roleObject.getRole();
            if (role.hasMoreRightsThanAscendants(roleObject) || role.hasLessRightsThanDescendants(roleObject)) {
                throw new RoleObjectConsistencyException();
            }
        }
    }

    /**
     * The hint message will be returned and cleared after that
     *
     * @return the hint message
     */
    @ModelAttribute("hintMessage")
    public HintMessage getHintMessage() {
        HintMessage message = hintMessage;
        hintMessage = null;
        return message;
    }

    /**
     * A controller can use this method to set a hint message
     *
     * @param hintMessage the hint message to set
     */
    protected void setHintMessage(HintMessage hintMessage) {
        this.hintMessage = hintMessage;
    }

    /**
     * Short cut method for checking read rights for the current user on an object presented by object id
     *
     * @param objectId the object's id
     * @return true if the user can read
     */
    public boolean canRead(Long objectId) {
        return canRead(dataObjectRepository.findOne(objectId));
    }

    /**
     * Short cut method for checking write rights for the current user on an object presented by object id
     *
     * @param objectId the object's id
     * @return true if the user can write
     */
    public boolean canWrite(Long objectId) {
        return canWrite(dataObjectRepository.findOne(objectId));
    }

    /**
     * Short cut method for checking delete rights for the current user on an object presented by object id
     *
     * @param objectId the object's id
     * @return true if the user can delete
     */
    public boolean canDelete(Long objectId) {
        return canDelete(dataObjectRepository.findOne(objectId));
    }

    /**
     * Short cut method for checking read rights for the current user on an object
     *
     * @param object the object to check
     * @return true if the user can read
     */
    public boolean canRead(DataObject object) {
        return getCurrentUser().canRead(object);
    }

    /**
     * Short cut method for checking write rights for the current user on an object
     *
     * @param object the object to check
     * @return true if the user can write
     */
    public boolean canWrite(DataObject object) {
        return getCurrentUser().canWrite(object);
    }

    /**
     * Short cut method for checking delete rights for the current user on an object
     *
     * @param object the object to check
     * @return true if the user can delete
     */
    public boolean canDelete(DataObject object) {
        return getCurrentUser().canDelete(object);
    }

}
