package de.unileipzig.bis.rbs.testApp.model;

import javax.persistence.*;

/**
 * Object database entity model.
 *
 * @author Stephan Kemper
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="table_name")
@Table(name="rbs_objects")
public class Object {

    /**
     * Database id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Long id;

    /**
     * Empty constructor as required in JPA
     */
    public Object() { }


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return String.format("Object [id=%d]", id);
    }
}