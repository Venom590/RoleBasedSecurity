package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;
import java.util.Set;

/**
 * DataObject database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Table(name="rbs_objects")
public class DataObject {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * DataObject name
     */
    @Column
    private String name;

    /**
     * Roles
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "rbs_roles_objects", joinColumns = {
            @JoinColumn(name = "object_id", nullable = false)
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false)
    })
    private Set<Role> roles;

    /**
     * Empty constructor as required in JPA
     */
    public DataObject() { }

    /**
     * Data constructor
     *
     * @param name the name
     */
    public DataObject(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return String.format("DataObject [id=%d, name=%s]", id, name);
    }
}