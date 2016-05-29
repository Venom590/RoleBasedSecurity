package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DataObject database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "table_name")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id.object", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleObject> roleObjects = new HashSet<>(0);

    /**
     * Empty constructor as required in JPA
     */
    public DataObject() { }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the roles
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<RoleObject> getRoleObjects() {
        return roleObjects;
    }

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
}