package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * Object database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Table(name="rbs_objects")
public class Object {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Object name
     */
    @Column
    private String name;

    /**
     * Empty constructor as required in JPA
     */
    public Object() { }

    /**
     * Data constructor
     *
     * @param name the name
     */
    public Object(String name) {
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

    @Override
    public String toString() {
        return String.format("Object [id=%d, name=%s]", id, name);
    }
}