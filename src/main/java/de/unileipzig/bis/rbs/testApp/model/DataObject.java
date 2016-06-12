package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DataObject database entity model.
 *
 * @author Stephan Kemper
 * @author Lukas Werner
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="rbs_objects")
public class DataObject {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Long id;

    /**
     * Roles
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rbs_roles_objects", joinColumns = {
            @JoinColumn(name = "object_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false)
    })
    private Set<Role> roles = new HashSet<>(0);

    /**
     * The role objects (association table)
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.object", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleObject> roleObjects = new HashSet<>(0);

    /**
     * Empty constructor as required in JPA
     */
    public DataObject() { }

    /**
     * Getter for id
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for roles
     *
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Setter for roles
     *
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Getter for roleObjects
     *
     * @return the roleObjects
     */
    public Set<RoleObject> getRoleObjects() {
        return roleObjects;
    }

    /**
     * Setter for roleObjects
     *
     * @param roleObjects the roleObjects to set
     */
    public void setRoleObjects(Set<RoleObject> roleObjects) {
        this.roleObjects = roleObjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataObject that = (DataObject) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("DataObject [id=%d]", id);
    }

    @PreRemove
    public void preRemove() {
        this.roles = null;
        this.roleObjects = null;
    }
}