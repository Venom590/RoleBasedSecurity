package de.unileipzig.bis.rbs.testApp.model;


import org.hibernate.annotations.Check;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Role database entity model.
 *
 * @author Stephan Kemper
 * @author Lukas Werner
 */
@Entity
@Table(name="rbs_roles")
@Check(constraints = "parent_id <> id")
public class Role {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Parent role
     */
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private Role parentRole;

    /**
     * Set of roles of children
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentRole")
    private Set<Role> childRoles = new HashSet<>(0);

    /**
     * Name
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Users
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rbs_users_roles", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "user_id", nullable = false)
    })
    private Set<User> users = new HashSet<>(0);

    /**
     * Objects
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rbs_roles_objects", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "object_id", nullable = false)
    })
    private Set<DataObject> objects = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleObject> roleObjects = new HashSet<>(0);

    /**
     * Empty constructor as required in JPA
     */
    public Role() { }

    /**
     * Data constructor
     *
     * @param parentRole the parent role
     * @param name the name
     */
    public Role(String name, Role parentRole) {
        this.parentRole = parentRole;
        this.name = name;
    }

    /**
     * Constructor with id for test purposes
     *
     * @param id the id
     * @param parentRole the parent role
     * @param name the name
     */
    public Role(Long id, String name, Role parentRole) {
        this.id = id;
        this.parentRole = parentRole;
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }


    /**
     * @return the parent role object
     */
    public Role getParentRole() {
        return parentRole;
    }

    /**
     * @param parentRole the parent role object
     */
    public void setParentRole(Role parentRole) {
        this.parentRole = parentRole;
    }

    /**
     * @return set of the child role objects
     */
    public Set<Role> getChildRoles() {
        return childRoles;
    }

    /**
     * @param childRoles set the child role objects
     */
    public void setChildRoles(Set<Role> childRoles) {
        this.childRoles = childRoles;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * @return the dataObjects
     */
    public Set<DataObject> getObjects() {
        return objects;
    }

    /**
     * @param objects the dataObjects to set
     */
    public void setObjects(Set<DataObject> objects) {
        this.objects = objects;
    }

    /**
     * @return the parent role id
     */
    public Long getParentId() {
        if (parentRole != null) {
            return parentRole.getId();
        } else {
            return null;
        }
    }

    /**
     * Pre Remove to set ON DELETE SET NULL
     */
    @PreRemove
    public void preRemove(){
        this.parentRole = null;
    }

    public Set<RoleObject> getRoleObjects() {
        return roleObjects;
    }

    public void setRoleObjects(Set<RoleObject> roleObjects) {
        this.roleObjects = roleObjects;
    }

    public boolean canRead(DataObject object) {
        for (RoleObject roleObject: roleObjects) {
            if (roleObject.getObject().equals(object) && roleObject.getCanRead()) {
                return true;
            }
        }
        return false;
    }

    public boolean canWrite(DataObject object) {
        for (RoleObject roleObject: roleObjects) {
            if (roleObject.getObject().equals(object) && roleObject.getCanWrite()) {
                return true;
            }
        }
        return false;
    }

    public boolean canDelete(DataObject object) {
        for (RoleObject roleObject: roleObjects) {
            if (roleObject.getObject().equals(object) && roleObject.getCanDelete()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find the root in this role tree (can be the role itself)
     *
     * @return the root role
     */
    public Role findRoot() {
        if (parentRole != null) {
            return parentRole.findRoot();
        }
        return this;
    }

    /**
     * Recursive method to actually find the descendants
     *
     * @param descendants the Set of roles to collect the descendants
     */
    private void findDescendants(Set<Role> descendants) {
        if (childRoles != null) {
            descendants.addAll(childRoles);
            for (Role r : childRoles) {
                r.findDescendants(descendants);
            }
        }
    }

    /**
     * Find all descendants of this role including itself
     *
     * @return the descendants of this role
     */
    public Set<Role> findDescendants() {
        Set<Role> descendants = new HashSet<>();
        descendants.add(this);
        findDescendants(descendants);
        return descendants;
    }

    /**
     * Recursive method to actually find the ascendants
     *
     * @param ascendants the Set of roles to collect the ascendants
     */
    private void findAscendants(Set<Role> ascendants) {
        if (parentRole != null) {
            ascendants.add(parentRole);
            parentRole.findAscendants(ascendants);
        }
    }

    /**
     * Find all ascendants of this role including itself
     *
     * @return the ascendants of this role
     */
    public Set<Role> findAscendants() {
        Set<Role> ascendants = new HashSet<>();
        ascendants.add(this);
        findAscendants(ascendants);
        return ascendants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return id != null ? id.equals(role.id) : role.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Role [id=%d, parent=%s, name=%s]", id, parentRole, name);
    }
}